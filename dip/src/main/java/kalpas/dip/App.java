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

        CLayer layer1 = new CLayer(24, 7, trainingSet.columns);
        Flayer layer2 = new Flayer(10, 4032);

        Image image = null;
        for(int i = 0; i < 5; i++)
        {
            trainingSet = new TrainingSet();
            for(int j = 0; j < 10; j++)
            {
                image = trainingSet.getNextImage();
                layer2.process(process(layer1.process(image.bytes)));
//                System.out.println(layer2.process(process(layer1.process(image.bytes))));
                //layer1.backPropagate(layer2.backPropagate(new double[]{}));
            }
        }


        Visualize.drawInput(image.bytes, trainingSet.columns);
        Visualize.draw1Layer(layer1.featureMaps, layer1.featureMapSize);
        // visualize.repaint();

    }

    private static double[] process(double[][] inputs)
    {
        double[] result = new double[inputs.length * inputs[0].length];
        for(int i = 0; i < inputs.length; i++)
            System.arraycopy(inputs[i], 0, result, i*inputs[i].length, inputs[i].length);
            
        return result;
    }

}
