package kalpas.test;

import java.util.Arrays;

public class Test
{

    @org.junit.Test
    public void testNetwork()
    {
        int inputs = 3;

        testNeuronLayer layer = new testNeuronLayer(3, 3);

        double[] input = null;
        for(int i = 0; i < 3; i++)
        {
            input = new double[inputs];
            Arrays.fill(input, 0);
            input[i] = 1;

            
            layer.setWeights(new double[][]
                  { { 1.0, 0.0, 0.0, 1.0},
                    { 0.0, 1.0, 0.0, 1.0 },
                    { 0.0, 0.0, 1.0, 1.0 } });

            layer.process(input);

            System.out.println("input: " + Arrays.toString(input)
                    + ", outputs: " + Arrays.toString(layer.getOutput()));
            
        }
    }
}
