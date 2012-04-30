package kalpas.dip.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import kalpas.dip.DataSet;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.TestSet;
import kalpas.dip.TrainingSet;

public class Trainer
{
    private NeuralNetwork net;

    private DataSet       trainSet      = new TrainingSet();
    private DataSet       testSet       = new TestSet();

    private Double        ETA           = Constants.ETA_LEARNIG_RATE;
    private int           EPOCHS        = 100;
    private double        errorThresold = 0.001;

    private DataSet       primarySet    = null;

    private Trainer()
    {
    }

    public static Trainer train(NeuralNetwork network)
    {
        final Trainer trainer = new Trainer();
        trainer.net = network;
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
                dump(i);
                Constants.ETA_LEARNIG_RATE /= 10;
            }
        }
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
            oos.writeObject(net);
            oos.flush();
            oos.close();
        }
        catch(IOException e)
        {
            System.err.println("smth bad with IO " + e.getStackTrace());
        }
    }

    protected void dump(int epoch)
    {
        File dumpDir = new File("dump");
        dumpDir.mkdirs();
        String fileName = "dump\\" + net.getClass().toString() + "epoch"
                + epoch + "." + (new Date()).getTime();
        save(fileName);
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
}
