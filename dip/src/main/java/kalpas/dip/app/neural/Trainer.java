package kalpas.dip.app.neural;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.swing.JFrame;

import kalpas.dip.app.data.DataSet;
import kalpas.dip.app.data.Image;
import kalpas.dip.app.data.TestSet;
import kalpas.dip.app.data.TrainingSet;
import kalpas.dip.app.neural.networks.NeuralNetwork;

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

    private Double            ETA              = Core.ETA_LEARNIG_RATE;
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
        return trainer;

    }

    public void reinit()
    {
        trainSet = new TrainingSet();
        testSet = new TestSet();
        this.primarySet = this.trainSet;
        this.dir = new File("dump\\" + (new Date()).getTime());
        this.dir.mkdirs();
    }

    public Trainer onTrainSet()
    {
        this.primarySet = trainSet;
        return this;
    }

    public Trainer onTestSet()
    {
        this.primarySet = testSet;
        return this;
    }

    public void start(int epochs)
    {
        this.EPOCHS = epochs;
        this.start();

    }

    public void start()
    {
        System.out.println("Running test before start");
        test();
        
        double percent = 0;
        int percentStep = 0;
        epochTimes = new long[EPOCHS];
        averageMSE = new BigDecimal[this.EPOCHS];
        this.trainMSE = new double[this.EPOCHS][this.trainSet.imageCount];
        startTrainig = new Date();
        startEpoch = startTrainig;
        if(primarySet != null)
        {
            Image image = null;
            for(int i = 0; i < EPOCHS; i++)
            {
                System.out.println("ETA is: " + Core.ETA_LEARNIG_RATE);
                averageMSE[i] = BigDecimal.ZERO;
                percent = 0;
                percentStep = 0;
                for(int j = 0; j < primarySet.imageCount; j++)
                {
                    image = primarySet.getNextImage();
                    net.process(image);
                    double mse = net.getMSE();
                    trainMSE[i][j] = mse;
                    averageMSE[i] = averageMSE[i].add(BigDecimal.valueOf(mse));
                    if(!net.isFault() && mse < Core.ERROR_THRESOLD)
                        continue;
                    net.backPropagate();

                    percent = 100.0 * j / primarySet.imageCount;
                    if(Math.floor(percent) > percentStep)
                    {
                        percentStep++;
                        System.out.println(percentStep + " %");

                    }
                }
                endEpoch = new Date();
                epochTimes[i] = endEpoch.getTime() - startEpoch.getTime();
                dump(i);
                Core.ETA_LEARNIG_RATE *= 0.95;
                System.out.println("EPOCH " + i + " is finished");
                averageMSE[i] = averageMSE[i].divide(
                        BigDecimal.valueOf(primarySet.imageCount), 15,
                        RoundingMode.HALF_UP);
                viewMSE(getTrainMSE()[i], "MSE for " + i + " EPOCH",
                        20d);
                System.out.println("Average MSE for " + i + " EPOCH is: "
                        + averageMSE[i].doubleValue());
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

            for(int i = 0; i < averageMSE.length; i++)
                System.out.println("Average mse per EPOCH "+i+" = "+averageMSE[i].doubleValue());
            viewMSE(trainMSE, "MSE per all EPOCHs", 20d);
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
        double percent = 0;
        int percentStep = 0;
        
        this.testMSE = new double[this.testSet.imageCount];
        int errors = 0;
        Image image = null;
        int output = -1;
        BigDecimal avMSE = BigDecimal.ZERO;
        for(int j = 0; j < testSet.imageCount; j++)
        {
            image = testSet.getNextImage();
            output = net.process(image);
            double MSE = net.getMSE();
            testMSE[j] = MSE;
            avMSE = avMSE.add(BigDecimal.valueOf(MSE));
            if(output != image.value)
            {
                Core.out.println("pattern not recognized: " + image.index);
                errors++;
            }
            percent = 100.0 * j / testSet.imageCount;
            if(Math.floor(percent) > percentStep)
            {
                percentStep++;
                System.out.println(percentStep + " %");

            }
        }
        System.out.println(errors + " - " + errors * 100 / testSet.imageCount
                + "%");
        avMSE = avMSE.divide(BigDecimal.valueOf(testSet.imageCount), 15,
                RoundingMode.HALF_UP);
        System.out.println("Average MSE is: " + avMSE.doubleValue());
        viewMSE(getTestMSE(), "MSE during test", 20d);
        return errors;

    }

    public int test(Image image)
    {
        int output = -1;
        long startTime = System.currentTimeMillis();
        output = net.process(image);
        long endTime = System.currentTimeMillis();
        System.out
                .println("Time to recognise: " + (endTime - startTime) + "ms");
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

    public void viewMSE(double[] mse, String label, double max)
    {
        HistogramDataset histogramdataset = new HistogramDataset();
        histogramdataset.addSeries("MSE", mse, 100, Core.ERROR_THRESOLD,
                max);

        JFreeChart jfreechart = createChart(histogramdataset, label);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setVisible(true);

        JFrame frame = new JFrame("MSE");
        frame.setSize(800, 800);
        frame.getContentPane().add(chartpanel);
        frame.setVisible(true);

    }
    
    public void viewMSE(double[][] mse, String label, double max)
    {
        HistogramDataset histogramdataset = new HistogramDataset();
        for(int i = 0; i < mse.length; i++)
        {
            histogramdataset.addSeries("EPOCH "+i, mse[i], 100, Core.ERROR_THRESOLD,
                max);
        }

        JFreeChart jfreechart = createChart(histogramdataset, label);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setVisible(true);

        JFrame frame = new JFrame("MSE");
        frame.setSize(800, 800);
        frame.getContentPane().add(chartpanel);
        frame.setVisible(true);

    }

    private static JFreeChart createChart(IntervalXYDataset intervalxydataset,
            String label)
    {
        JFreeChart jfreechart = ChartFactory.createHistogram(label, null, null,
                intervalxydataset, PlotOrientation.VERTICAL, true, true, false);
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
