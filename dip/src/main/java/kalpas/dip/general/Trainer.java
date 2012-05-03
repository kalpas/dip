package kalpas.dip.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.JFrame;

import kalpas.dip.DataSet;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.TestSet;
import kalpas.dip.TrainingSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class Trainer implements Serializable
{
    private static final long serialVersionUID = 1L;

    private NeuralNetwork     net;

    transient private DataSet trainSet         = new TrainingSet();
    transient private DataSet testSet          = new TestSet();

    private Double            ETA              = Constants.ETA_LEARNIG_RATE;
    private int               EPOCHS           = 10;

    transient private DataSet primarySet       = null;

    private Date              startTrainig     = null;
    private Date              endTraining      = null;
    private long              delta;
    private long[]            epochTimes;
    private long              averagePerEpoch;
    private Date              startEpoch       = null;
    private Date              endEpoch         = null;

    private double[][]        trainMSE;
    private double[]          testMSE;
    private BigDecimal[]      averageMSE       = null;

    private File              dir              = null;

    private DecimalFormat     df               = new DecimalFormat("#0.##");

    private Trainer()
    {
    }

    public static Trainer train(NeuralNetwork network)
    {
        final Trainer trainer = new Trainer();
        trainer.net = network;
        trainer.primarySet = trainer.trainSet;
        trainer.dir = new File("dump\\" + (new Date()).getTime());
        trainer.dir.mkdirs();
        trainer.trainMSE = new double[trainer.EPOCHS][trainer.trainSet.imageCount];
        trainer.testMSE = new double[trainer.testSet.imageCount];
        return trainer;

    }

    public void reinit()
    {
        trainSet = new TrainingSet();
        testSet = new TestSet();
        this.primarySet = this.trainSet;
        this.dir = new File("dump\\" + (new Date()).getTime());
        this.dir.mkdirs();
        this.trainMSE = new double[this.EPOCHS][this.trainSet.imageCount];
        this.testMSE = new double[this.testSet.imageCount];
        this.averageMSE = new BigDecimal[this.EPOCHS];
        df = new DecimalFormat("#0.##");

    }

    public Trainer onTrainSet()
    {
        this.primarySet = trainSet;
        this.trainMSE = new double[this.EPOCHS][this.primarySet.imageCount];
        return this;
    }

    public Trainer onTestSet()
    {
        this.primarySet = testSet;
        this.trainMSE = new double[this.EPOCHS][this.primarySet.imageCount];
        return this;
    }

    public void start(int epochs)
    {
        this.EPOCHS = epochs;
        this.start();

    }

    public void start()
    {
        epochTimes = new long[EPOCHS];
        startTrainig = new Date();
        startEpoch = startTrainig;
        if(primarySet != null)
        {
            Image image = null;
            for(int i = 0; i < EPOCHS; i++)
            {
                averageMSE[i] = BigDecimal.ZERO;
                for(int j = 0; j < primarySet.imageCount; j++)
                {
                    image = primarySet.getNextImage();
                    net.process(image);
                    double mse = net.getMSE();
                    trainMSE[i][j] = mse;
                    averageMSE[i].add(BigDecimal.valueOf(mse));
                    if(!net.isFault() && mse < Constants.ERROR_THRESOLD)
                        continue;
                    net.backPropagate();

                    System.out.println(df.format(100.0 * j
                            / primarySet.imageCount)
                            + " %");
                }
                endEpoch = new Date();
                epochTimes[i] = endEpoch.getTime() - startEpoch.getTime();
                dump(i);
                Constants.ETA_LEARNIG_RATE *= 0.8;
                System.out.println("EPOCH " + i + " is finished");
                viewMSE(getTrainMSE()[i]);
                averageMSE[i].divide(BigDecimal.valueOf(primarySet.imageCount));
                test();
                startEpoch = new Date();

            }
            endTraining = endEpoch;

            averagePerEpoch = 0l;
            for(long epoch : epochTimes)
            {
                averagePerEpoch += epoch;
            }
            averagePerEpoch /= epochTimes.length;

            delta = endTraining.getTime() - startTrainig.getTime();
            
            double[] avMSE = new double[EPOCHS];
            int i = 0;
            for(BigDecimal val: averageMSE)
                avMSE[i++] = val.doubleValue();
            viewMSE(avMSE);
            System.out.println(this.toString());
            save();
        }
    }

    public String getDeltaTime()
    {
        return extractTime(delta);
    }

    private String extractTime(long value)
    {
        String time = "";
        time = value % 1000 + time;
        value /= 1000l;
        time = value % 60 + ":" + time;
        value /= 60l;
        time = value % 60 + ":" + time;
        return time;
    }

    public int test()
    {
        int errors = 0;
        Image image = null;
        int output = -1;
        for(int j = 0; j < testSet.imageCount; j++)
        {
            image = testSet.getNextImage();
            output = net.process(image);
            double MSE = net.getMSE();
            testMSE[j] = MSE;
            if(output != image.value)
            {
                System.err.println("pattern not recognized: " + image.index);
                errors++;
            }
            System.out
                    .println(df.format(100.0 * j / testSet.imageCount) + " %");
        }
        System.out.println(errors + " - " + errors * 100 / testSet.imageCount
                + "%");
        viewMSE(getTestMSE());
        return errors;

    }

    public int test(Image image)
    {
        int output = -1;
        output = net.process(image);
        System.out.println("digit is: " + output);
        System.out.println("MSE: " + net.getMSE());
        if(output != image.value)
        {
            System.err.println("ups!!! wrong guess: " + image.value);
        }
        return output;

    }

    public void save(String name)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        }
        catch(IOException e)
        {
            System.err.println("smth bad with IO " + e.getCause());
        }
    }

    private void save()
    {
        save(dir.getPath() + "\\" + this.getClass().getName());
    }

    public static Trainer load(String name)
    {
        try
        {
            FileInputStream fis = new FileInputStream(name);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Trainer trainer = (Trainer) oin.readObject();
            return trainer;
        }
        catch(Exception e)
        {
            System.err.println("smth bad " + e.getCause());
            return null;
        }
    }

    protected void dump(int epoch)
    {
        String fileName = dir.getPath() + "\\" + this.getClass().getName()
                + ".epoch" + epoch;
        save(fileName);
    }

    @Override
    public String toString()
    {
        String newline = System.getProperty("line.separator");

        StringBuilder string = new StringBuilder();
        string.append("EPOCHS: " + EPOCHS);
        string.append(newline);
        string.append("trainig time: " + getDeltaTime());
        string.append(newline);
        string.append("Timings per epoch");
        string.append(newline);
        for(long value : epochTimes)
        {
            string.append(extractTime(value));
            string.append(" ");
        }
        string.append(newline);
        string.append("average per epoch: " + extractTime(averagePerEpoch));
        return string.toString();
    }

    public void viewMSE(double[] mse)
    {
        HistogramDataset histogramdataset = new HistogramDataset();
        histogramdataset.addSeries("MSE", mse, 1000, Constants.ERROR_THRESOLD, 20D);

        JFreeChart jfreechart = createChart(histogramdataset);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setVisible(true);
        
        JFrame frame = new JFrame("MSE");
        frame.setSize(800, 800);
        frame.getContentPane().add(chartpanel);
        frame.setVisible(true);

    }

    private static JFreeChart createChart(IntervalXYDataset intervalxydataset)
    {
        JFreeChart jfreechart = ChartFactory.createHistogram(
                "MSE per current EPOCH", null, null, intervalxydataset,
                PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setDomainPannable(true);
        xyplot.setRangePannable(true);
        xyplot.setForegroundAlpha(0.85F);
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
        xybarrenderer.setDrawBarOutline(false);
        xybarrenderer.setBarPainter(new StandardXYBarPainter());
        xybarrenderer.setShadowVisible(false);
        return jfreechart;
    }

    public void viewNetwork(int n, boolean fromTestSet)
    {
        DataSet dataSet = fromTestSet ? testSet : trainSet;
        Image image = dataSet.getImageBy(n);
        net.viewNetwork(image);

    }

    /**
     * @return the eTA
     */
    public Double getETA()
    {
        return ETA;
    }

    /**
     * @param eTA
     *            the eTA to set
     */
    public void setETA(Double eTA)
    {
        ETA = eTA;
    }

    /**
     * @return the ePOCHS
     */
    public int getEPOCHS()
    {
        return EPOCHS;
    }

    /**
     * @param ePOCHS
     *            the ePOCHS to set
     */
    public void setEPOCHS(int ePOCHS)
    {
        EPOCHS = ePOCHS;
    }


    /**
     * @return the delta
     */
    public long getDelta()
    {
        return delta;
    }

    /**
     * @return the dir
     */
    public File getDir()
    {
        return dir;
    }

    /**
     * @param dir
     *            the dir to set
     */
    public void setDir(File dir)
    {
        this.dir = dir;
    }

    /**
     * @return the net
     */
    public NeuralNetwork getNet()
    {
        return net;
    }

    /**
     * @return the trainMSE
     */
    public double[][] getTrainMSE()
    {
        return trainMSE;
    }

    /**
     * @return the testMSE
     */
    public double[] getTestMSE()
    {
        return testMSE;
    }

    /**
     * @return the trainSet
     */
    public DataSet getTrainSet()
    {
        return trainSet;
    }

    /**
     * @return the testSet
     */
    public DataSet getTestSet()
    {
        return testSet;
    }
}
