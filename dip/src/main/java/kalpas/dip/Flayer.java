package kalpas.dip;

import java.util.Arrays;
import java.util.Random;

import kalpas.dip.general.Constants;

public class Flayer
{
    private double[]   dErrorDy;
    private double[][] dErrorDw;
    private double [] dErrorDxm1;
    
    private double[] input;
    private double[][] weights;
    private double[]   output;
    
    private int        neurons;
    private int        inputs;
    
    private int BIAS;

    public Flayer(int neurons, int inputs)
    {
        this.BIAS =inputs;
        this.neurons = neurons;
        this.inputs = inputs;
        weights = new double[neurons][inputs+1];
        output = new double[neurons];
        dErrorDy = new double[neurons];
        dErrorDw = new double[neurons][inputs+1];
        dErrorDxm1 = new double[neurons];
        
        initWeights();
        
    }

    public double[] process(double[] inputData)
    {
        this.input = inputData;
        double sum = 0;
        for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
        {
            sum = weights[neuronIndex][BIAS];
            for(int inputIndex = 0; inputIndex < inputs; inputIndex++)
            {
                sum += input[inputIndex] * weights[neuronIndex][inputIndex];
            }
            output[neuronIndex] = activationFunction(sum);
        }
        return output;
    }

    public double[] backPropagate(double[] dErrorDx)
    {
        double dEdY;
        Arrays.fill(dErrorDxm1, 0.0);
        for(double[] array: dErrorDw)
            Arrays.fill(array,0.0);
        for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
        {
            dEdY = activationFunctionDerivative(neuronIndex)* dErrorDx[neuronIndex];
            dErrorDy[neuronIndex] = dEdY;
            dErrorDw[neuronIndex][BIAS] = dEdY;
            for(int connectIndex = 0; connectIndex < inputs; connectIndex++)
            {
                dErrorDw[neuronIndex][connectIndex] += dEdY*input[connectIndex];
                dErrorDxm1[connectIndex]+=dEdY*weights[neuronIndex][connectIndex];
            }
            
        }
        
        for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
        {
            for(int connectionIndex = 0; connectionIndex < inputs; connectionIndex++)
            {
                weights[neuronIndex][connectionIndex] -= Constants.ETA_LEARNIG_RATE*dErrorDw[neuronIndex][connectionIndex];
            }
        }
        
        return dErrorDxm1;

    }
    
    private double activationFunction(double value)
    {
        return Math.tanh(value);
    }
    
    private double activationFunctionDerivative(int index)
    {
        return 1 - Math.pow(output[index], 2);
    }

    private void initWeights()
    {
        Random randomizer = new Random();
        final double pow = Math.pow(inputs, -0.5);
        for(double[] array: weights)
            for(int i = 0; i < inputs;i++)
                array[i] = randomizer.nextGaussian()*pow;
    }
    
    /**
     * @return the weights
     */
    public double[][] getWeights()
    {
        return weights;
    }

    /**
     * @param weights
     *            the weights to set
     */
    public void setWeights(double[][] weights)
    {
        this.weights = weights;
    }

    /**
     * @return the output
     */
    public double[] getOutput()
    {
        return output;
    }
}
