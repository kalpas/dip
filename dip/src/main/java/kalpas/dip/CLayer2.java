package kalpas.dip;

import java.util.Arrays;

import kalpas.dip.general.Constants;

public class CLayer2
{
    private double[][] bias;

    private double[][] weights;

    private double[][] featureMaps;

    public int         featureMapCount;

    public int         featureMapSize;

    private int        inputSize;

    public CLayer2(int featureMapSize, int featureMapCount, int inputSize)
    {
        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        weights = new double[featureMapCount][Constants.KERNEL_SIZE
                * Constants.KERNEL_SIZE + 1];
        featureMaps = new double[featureMapCount][featureMapSize
                * featureMapSize];
        bias = new double[featureMapCount][featureMapSize * featureMapSize];
        initKernelWeights();
        initBias();

    }

    public double[][] process(double[][] inputs)
    {
        return null;
    }

    private double activationFunction(double n)
    {
        return Math.tanh(n);
    }

    private final void initKernelWeights()
    {
        for(double[] array : weights)
        {
            Arrays.fill(array, 0.0);
        }
    }

    private final void initBias()
    {
        for(double[] array : bias)
        {
            Arrays.fill(array, 0.0);
        }
    }

}
