package kalpas.test;

import java.util.Arrays;
import java.util.Random;

public class Test
{

    @org.junit.Test
    public void testNetwork()
    {
        int inputs = 3;

        testNeuronLayer layer = new testNeuronLayer(3, 3);
        testNeuronLayer layer2 = new testNeuronLayer(3, 3);

        double[] input = null;
            input = new double[inputs];
            /*layer.setWeights(new double[][]
                  { { 1.0, 0.0, 0.0, 1.0},
                    { 0.0, 1.0, 0.0, 1.0 },
                    { 0.0, 0.0, 1.0, 1.0 } });*/

            layer2.process(layer.process(input));

            System.out.println("input: " + Arrays.toString(input)
                    + ", outputs: " + Arrays.toString(layer2.getOutput()));
            System.out.println();
            
            double[] output;
            double[] desired = new double[3];
            for (int i = 0 ; i < 10000; i++)
            {
                for(int k = 0; k < 3; k++)
                {
                    Arrays.fill(input, 0);
                    input[k] = 1; 
                    output = layer2.process(layer.process(input));
                    Arrays.fill(desired, 0);
                    desired[k%2] = 1;
                    layer.backPropagate(layer2.backPropagate(substract(output, desired)));
                }
                
            }
            
            Arrays.fill(input, 0);
            input[0] = 1; 
            layer2.process(layer.process(input));

            System.out.println("input: " + Arrays.toString(input)
                    + ", outputs: " + Arrays.toString(layer2.getOutput()));
            System.out.println();
        
    }
    
    @org.junit.Test
    public void t()
    {
        Random r = new Random();
        r.nextGaussian();
    }
    
    public static double[] substract(double[] a, double[] b)
    {
        double [] result = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i] - b[i];
        }
        return result;
    }
}
