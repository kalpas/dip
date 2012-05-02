package kalpas.dip.app;

import java.awt.Canvas;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;

import kalpas.dip.DataSet;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.complicated.NeuralNet;
import kalpas.dip.general.Trainer;

public class NeuralNetworkBanch
{
    
    private static Canvas imageCanvas = null;
    private static int[][] points;
    
    private static Trainer trainer;
    
    private double ETA = 0.001;
    
    private double mseMin = 0.001;

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

    public static boolean newNet(String... args)
    {
        NeuralNetwork net = new NeuralNet();
        trainer = Trainer.train(net).onTrainSet();
        return true;
    }

    public static boolean showPattern(String... args)
    {
        boolean result = false;
        String set = args[0];
        int n = 0;
        try
        {
            n = Integer.parseInt(args[1]);
        }
        catch(Exception e)
        {
        }

        Image pattern = null;
        DataSet dataSet = null;

        if(trainer != null && set.equals("trainSet"))
        {
            dataSet = trainer.getTrainSet();
        }
        else
            if(trainer != null && set.equals("testSet"))
            {
                dataSet = trainer.getTestSet();
            }
            else
            {
                System.err.println("wrong value for desired data set");
            }

        if(dataSet != null && n < dataSet.imageCount)
        {
            pattern = dataSet.getImageBy(n);
        }

        if(pattern != null)
        {
            JFrame frame = new JFrame(""+pattern.value);
            PatternPane pane = new PatternPane();
            final int magnification = pane.getMAGNIFICATION();
            frame.setSize(dataSet.columns*magnification,dataSet.rows*magnification+30);
            pane.setPattern(pattern);
            frame.getContentPane().add(pane);
            frame.setVisible(true);
            result = true;
        }
        return result;
    }
    
    public static boolean drawDigit()
    {
        boolean result = false;
        JFrame frame = new JFrame("Draw a digit");
        imageCanvas = new Canvas();
        imageCanvas.setBackground(new Color(0xFFFFFFFF));
        imageCanvas.setSize(w, h);
        points = new int[w][h];
        //-----
        imageCanvas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                canvasImageMouseDummy(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                canvasImageMouseDummy(evt);
            }
        });
        //---
        imageCanvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                canvasImageMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                canvasImageMouseDummy(evt);
            }
        });
        
        
        return result;
    }

    public static boolean setProperty(String... args)
    {
        
        return false;
    }
    
    //--------------
    
    private static void canvasImageMouseDummy(java.awt.event.MouseEvent evt) {                                         
        // TODO add your handling code here:       
    }                                        

    private static void canvasImageMouseDragged(java.awt.event.MouseEvent evt) {                                         
        int x = evt.getX();
        int y = evt.getY();       
        if (x >= 2 && y >= 2 && x <= imageCanvas.getWidth()-2 && y <= imageCanvas.getHeight()-2) {
            imageCanvas.getGraphics().setColor(Color.black);        
            imageCanvas.getGraphics().fillRect(x - 2, y - 2, 4, 4);           
            for (int i = x - 2; i <= x + 1; i++) {
                for (int j = y - 2; j <= y + 1; j++) {
                    points[i][j] = 1;
                }
            }
        }        
    }
}
