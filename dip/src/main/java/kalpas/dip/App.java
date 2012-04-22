package kalpas.dip;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * Hello world!
 * 
 */
public class App
{
    private static NeuralNetwork network;
    private static TrainingSet   trainingSet;
    private static TestSet       testSet;

    static
    {
        try
        {
            network = new NeuralNetwork();
            trainingSet = new TrainingSet();
            testSet = new TestSet();
        }
        catch(IOException e)
        {
            System.err.println("smth bad happened " + e.getStackTrace());
        }

    }

    public static void main(String[] args) throws IOException
    {
        final int featureMapSize = 13;
        final int featureMapCount = 6;
        final int inputSize = 28;

        System.out.println("Hello neural network world!");

        JFrame frame = new JFrame("Frame in Java Swing");
        frame.setSize(400, 400);
        Visualize visualize = new Visualize();
        frame.getContentPane().add(visualize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        byte[] image = trainingSet.getImage();
        CLayer layer = new CLayer(featureMapSize, featureMapCount, inputSize);

        Visualize.drawInput(image, trainingSet.columns);
        double[][] data = layer.process(image);
        Visualize.draw1Layer(data, layer.featureMapSize);
        visualize.repaint();


    }
}
