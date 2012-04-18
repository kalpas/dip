package kalpas.dip;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    @Test
    public void testApp()
    {
        NeuralNetwork network = new NeuralNetwork();
        TrainingSet trainingSet = null;
        TestSet testSet = null;
        try
        {
            trainingSet = new TrainingSet();
            testSet = new TestSet();
            assertEquals(testSet.getLabel(),
                    network.process(testSet.getImage()));

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


