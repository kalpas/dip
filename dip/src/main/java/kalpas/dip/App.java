package kalpas.dip;

import java.io.IOException;

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

    public static void main(String[] args)
    {
        System.out.println("Hello neural network world!");
        try
        {
            network.process(testSet.getImage());
        }
        catch(IOException e)
        {
            System.err.println("smth bad happened " + e.getStackTrace());
        }
    }

}
