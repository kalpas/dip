package kalpas.dip.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import kalpas.dip.NeuralNetwork;
import kalpas.dip.complicated.NeuralNet;
import kalpas.dip.general.Trainer;

public class NeuralNetworkBanch
{

    private static Trainer trainer;

    public static boolean save(String... args)
    {
        return false;
    }

    public static boolean load(String... args)
    {
        boolean result = false;
        File file = new File(args[0]);

        if(!file.exists())
        {
            System.err.println("File doesn't exist");
            return result;
        }

        try
        {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(fis);
            trainer = (Trainer) oin.readObject();
        }
        catch(IOException e)
        {
            System.err.println("smth bad with I/O " + e.getCause());
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            System.err.println("smth bad deserializing class  " + e.getCause());
            e.printStackTrace();
        }

        if(trainer != null)
        {
            trainer.reinit();
            result = true;
        }
        else
        {
            System.err.println("nothing loaded )=");
        }

        return result;

    }

    public static boolean test(String... args)
    {
        boolean result = false;
        if(trainer != null)
        {
            trainer.test();
        }
        else
        {
            System.err.println("Network isn't loaded");
        }
        return result;
    }

    public static boolean train(String... args)
    {
        boolean result = false;
        int EPOCHS = 0;
        try
        {
            EPOCHS = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e)
        {
        }
        if(trainer != null && trainer.getNet() != null)
        {
            if(EPOCHS != 0)
            {
                trainer.start(EPOCHS);
            }
            else
            {
                trainer.start();
            }
            result = true;
        }
        else
        {
            System.err.println("network is not created/loaded yet");
        }
        return result;
    }
    
    public static boolean newNet(String ... args)
    {
        NeuralNetwork net = new NeuralNet();
        trainer= Trainer.train(net).onTrainSet();
        return true;
    }

    public static boolean setProperty(String... args)
    {
        return false;
    }
}
