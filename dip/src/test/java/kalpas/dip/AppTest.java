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

    @Test
    public void testLayer()
    {
        CLayer layer = new CLayer(13, 5, 28);
        assertEquals(3, layer.getKernelX(3));
        assertEquals(3, layer.getKernelX(8));
        assertEquals(0, layer.getKernelY(3));
        assertEquals(1, layer.getKernelY(8));
        assertEquals(1, layer.getKernelY(9));
    }

}
