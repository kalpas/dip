package kalpas.dip;

import java.io.IOException;

/**
 * Hello world!
 * 
 */
public class App
{
    public static void main(String[] args)
    {
        System.out.println("Hello neural network world!");
        TrainingSet trainingSet = null;
        TestSet testSet = null;
        try
        {
            trainingSet = new TrainingSet();
            testSet = new TestSet();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out
                .println((trainingSet == null ? "error occured" : trainingSet));
        System.out.println((testSet == null ? "error occured" : testSet));
    }
}
