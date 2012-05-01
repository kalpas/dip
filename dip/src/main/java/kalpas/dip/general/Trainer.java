package kalpas.dip.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import kalpas.dip.DataSet;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.TestSet;
import kalpas.dip.TrainingSet;

public class Trainer implements Serializable
{
    private static final long serialVersionUID = 1L;

    private NeuralNetwork net;

    transient private DataSet       trainSet      = new TrainingSet();
    transient private DataSet       testSet       = new TestSet();

    private Double        ETA           = Constants.ETA_LEARNIG_RATE;
    private int           EPOCHS        = 100;
    private double        errorThresold = 0.001;

    transient private DataSet       primarySet    = null;
    
    private Date startTrainig = null;
    private Date endTraining = null;
    private long delta;
    private long[] epochTimes;
    private long averagePerEpoch;
    private Date startEpoch = null;
    private Date endEpoch = null;
    
    private File dir = null;

    private Trainer()
    {
    }

    public static Trainer train(NeuralNetwork network)
    {
        final Trainer trainer = new Trainer();
        trainer.net = network;
        trainer.primarySet = trainer.trainSet;
        trainer.dir = new File("dump\\"+(new Date()).getTime());
        trainer.dir.mkdirs();
        return trainer;

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
        epochTimes = new long[EPOCHS];
        startTrainig= new Date();
        startEpoch = startTrainig;
        if(primarySet != null)
        {
            Image image = null;
            for(int i = 0; i < EPOCHS; i++)
            {
                for(int j = 0; j < primarySet.imageCount; j++)
                {
                    image = primarySet.getNextImage();
                    net.process(image);
                    if(!net.isFault()&&net.getdErrorDx()[image.value]<errorThresold)
                        continue;
                    net.backPropagate();
                }
                endEpoch = new Date();
                epochTimes[i] = endEpoch.getTime()- startEpoch.getTime();
                dump(i);
                Constants.ETA_LEARNIG_RATE /= 100;
                startEpoch = endEpoch;
                
            }
            endTraining = new Date();
            
            averagePerEpoch = 0l;
            for(long epoch: epochTimes)
            {
                averagePerEpoch +=epoch;
            }
            averagePerEpoch/= epochTimes.length;
            
            delta = endTraining.getTime() - startTrainig.getTime();
            
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
        time = value%1000 + time;
        value /= 1000l;
        time = value%60 + ":" + time;
        value/= 60l;
        time = value%60 +":"+time;
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
            if(output != image.value)
            {
                System.err.println("pattern not recognized: " + image.index);
                errors++;
            }
        }
        System.out.println(errors + " - " + errors * 100 / testSet.imageCount
                + "%");
        return errors;

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
        save(dir.getPath()+"\\"+ this.getClass().getName());
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
        String fileName = dir.getPath()+"\\"+ this.getClass().getName() + ".epoch"
                + epoch;
        save(fileName);
    }
    
    @Override
    public String toString()
    {
        String newline = System.getProperty("line.separator");
        
        StringBuilder string = new StringBuilder();
        string.append("EPOCHS: "+EPOCHS);
        string.append(newline);
        string.append("trainig time: "+getDeltaTime());
        string.append(newline);
        string.append("Timings per epoch");
        string.append(newline);
        for(long value:epochTimes)
        {
            string.append(extractTime(value));
            string.append(" ");
        }
        string.append(newline);
        string.append("average per epoch: " + extractTime(averagePerEpoch));
        return string.toString();
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
     * @return the errorThresold
     */
    public double getErrorThresold()
    {
        return errorThresold;
    }

    /**
     * @param errorThresold
     *            the errorThresold to set
     */
    public void setErrorThresold(double errorThresold)
    {
        this.errorThresold = errorThresold;
    }

    /**
     * @return the delta
     */
    public long getDelta()
    {
        return delta;
    }
}
