package kalpas.dip.app;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kalpas.dip.DataSet;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.complicated.NeuralNet;
import kalpas.dip.general.Core;
import kalpas.dip.general.Trainer;
import kalpas.dip.simple.SimpleNetwork;

public class NeuralNetworkBanch
{
    private static int     inputSize       = 28;
    private static int     imageCanvasSize = 196;
    private static JFrame  frame;
    private static Canvas  imageCanvas     = null;
    private static int     desiredOutput   = -1;
    private static int[][] points;
    

    private static Trainer trainer;

    private double         ETA             = Core.ETA_LEARNIG_RATE;

    private double         mseMin          = 0.000000001;

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
            result  = true;
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
            if(args.length>1)
            {
                if(args[1].toLowerCase().equals("ontestset"))
                {
                    trainer.onTestSet();
                    System.out.println("trainig on test set");
                }
            }
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
        NeuralNetwork net;
        if(args.length>0 && args[0]!=null && "convo".equals(args[0]))
        {
            if(args.length<4)
            {
                net = new NeuralNet();
            }
            else
            {
                try
                {
                    net = new NeuralNet(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                }
                catch(Exception e)
                {
                    System.err.println("bad bad bad. just have created using defaults.. you can give it a try");
                    net = new NeuralNet();
                }
            }
        }
        else
        {
            net = new SimpleNetwork();
        }
        
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
        else if(trainer != null && set.equals("testSet"))
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
            JFrame frame = new JFrame("" + pattern.value);
            frame.setLocation(500,500);
            PatternPane pane = new PatternPane();
            final int magnification = pane.getMAGNIFICATION();
            frame.setSize(dataSet.columns * magnification, dataSet.rows
                    * magnification + 30);
            pane.setPattern(pattern);
            frame.getContentPane().add(pane);
            frame.setVisible(true);
            result = true;
        }
        return result;
    }

    public static boolean drawDigit(String... args)
    {
        boolean result = false;
        if(trainer == null || frame!=null)
            return result;

        desiredOutput = -1;
        try
        {
            desiredOutput = Integer.parseInt(args[0]);
        }
        catch(Exception e)
        {
        }
        if(desiredOutput < 0 || desiredOutput > 9)
        {
            System.out.println("digits 0 - 9 are expected");
            return result;
        }

        frame = new JFrame("Draw a digit");
        frame.setSize(imageCanvasSize, imageCanvasSize + 30);
        frame.setResizable(false);
        imageCanvas = new Canvas();
        imageCanvas.setBackground(new Color(0xFFFFFFFF));
        imageCanvas.setSize(imageCanvasSize, imageCanvasSize);
        points = new int[imageCanvasSize][imageCanvasSize];
        // -----
        imageCanvas.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                canvasImageMouseDummy(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                canvasImageMouseDummy(evt);
            }
        });
        // ---
        imageCanvas
                .addMouseMotionListener(new java.awt.event.MouseMotionAdapter(){
                    public void mouseDragged(java.awt.event.MouseEvent evt)
                    {
                        canvasImageMouseDragged(evt);
                    }

                    public void mouseMoved(java.awt.event.MouseEvent evt)
                    {
                        canvasImageMouseDummy(evt);
                    }
                });

        imageCanvas.addKeyListener(new KeyListener(){

            public void keyTyped(KeyEvent e)
            {
                decode(e);
            }

            public void keyReleased(KeyEvent e)
            {
            }

            public void keyPressed(KeyEvent e)
            {
            }
        });

        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(imageCanvas,
                GroupLayout.PREFERRED_SIZE, imageCanvasSize,
                GroupLayout.PREFERRED_SIZE));

        layout.setVerticalGroup(layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(imageCanvas,
                GroupLayout.PREFERRED_SIZE, imageCanvasSize,
                GroupLayout.PREFERRED_SIZE));

        frame.setLocation(500,500);
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
        result = true;

        return result;
    }

    public static boolean setProperty(String... args)
    {
        boolean result = true;
        double eta = 0.0;
        double err = 0.0;
        
        try
        {
            eta = Double.parseDouble(args[0]);
            err = Double.parseDouble(args[1]);
        }
        catch(Exception e)
        {
            result = false;
        }
        if(result)
        {
            Core.ETA_LEARNIG_RATE = eta;
            Core.ERROR_THRESOLD = err;
            System.out.println("ETA_LEARNIG_RATE " + Core.ETA_LEARNIG_RATE);
            System.out.println("ERROR_THRESOLD " + Core.ERROR_THRESOLD);
        }
        
        return result;
    }
    
    public static boolean viewNetwork(String ...args)
    {
        boolean result = true;
        boolean fromTestSet = false;
        int n = 0;
        
        try{
            n = Integer.parseInt(args[1]);
            fromTestSet = Boolean.parseBoolean(args[0]);
        }
        catch(Exception e)
        {
            result = false;
        }
        
        if(!result)
        {
            System.err.println("wrong params");
            return result;
        }
        
        trainer.viewNetwork(n, fromTestSet);
        return result;
    }

    // --------------

    private static void decode(KeyEvent e)
    {
        if(e.getKeyChar() == '\n')
        {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
            imageCanvas = null;
            Image imageToTest = new Image(scale(points), desiredOutput, -1);
            points = null;
            trainer.test(imageToTest);
            JFrame frame = new JFrame("" + imageToTest.value);
            PatternPane pane = new PatternPane();
            final int magnification = pane.getMAGNIFICATION();
            frame.setSize(inputSize * magnification, inputSize
                    * magnification + 30);
            pane.setPattern(imageToTest);
            frame.getContentPane().add(pane);
            frame.setLocation(500,500);
            frame.setVisible(true);
        }
    }

    private static byte[] scale(int[][] input)
    {
        byte[] result = new byte[inputSize * inputSize];
        int bute = 0;
        for(int y = 0; y < inputSize; y++)
        {
            for(int x = 0; x < inputSize; x++)
            {
                bute = 0;
                for(int i = y * 7; i < y * 7 + 7; i++)
                {
                    for(int j = x * 7; j < x * 7 + 7; j++)
                    {
                        bute += input[i][j];
                    }
                }
                bute *= 255;
                bute /= 49;
                result[y * inputSize + x] = (byte) bute;
            }
        }
        return result;
    }

    private static void canvasImageMouseDummy(java.awt.event.MouseEvent evt)
    {
        // TODO add your handling code here:
    }

    private static void canvasImageMouseDragged(java.awt.event.MouseEvent evt)
    {
        int x = evt.getX();
        int y = evt.getY();
        if(x >= 6 && y >= 6 && x <= imageCanvas.getWidth() - 6
                && y <= imageCanvas.getHeight() - 6)
        {
            imageCanvas.getGraphics().setColor(Color.black);
            imageCanvas.getGraphics().fillRect(x - 6, y - 6, 12, 12);
            for(int i = x - 6; i <= x + 5; i++)
            {
                for(int j = y - 6; j <= y + 5; j++)
                {
                    points[j][i] = 1;
                }
            }
        }
    }
}
