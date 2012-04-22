package kalpas.dip;

import java.util.Arrays;

import kalpas.dip.general.Constants;

public class CLayer
{
    private double[][] bias;

    private double[][] weights;

    private double[][] featureMaps;

    public int         featureMapCount;

    public int         featureMapSize;

    private int        inputSize;


    public CLayer(int featureMapSize, int featureMapCount, int inputSize)
    {
        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        weights = new double[featureMapCount][Constants.KERNEL_SIZE * Constants.KERNEL_SIZE + 1];
        featureMaps = new double[featureMapCount][featureMapSize
                * featureMapSize];
        bias = new double[featureMapCount][featureMapSize * featureMapSize];
        initKernelWeights();
        initBias();

    }

    private double activationFunction(double n)
    {
        return Math.tanh(n);
    }

    public double[][] process(byte[] image)
    {
        int neuronCount = featureMapSize * featureMapSize;
        double sum = 0;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuron = 0; neuron < neuronCount; neuron++)
            {
                sum+= bias[feature][neuron];
                for(int kernelElement = 0; kernelElement < Constants.KERNEL_ELEMENTS; kernelElement++)
                {
                    // TODO optimize
                    sum += image[getImageIndex(getKernelX(kernelElement)
                            + getFeatureMapX(neuron), getKernelY(kernelElement)
                            + getFeatureMapY(neuron))]
                            * weights[feature][kernelElement];
                }
                featureMaps[feature][neuron] = activationFunction(sum);
                sum = 0;
            }
        }
        return featureMaps;
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

    protected final int getImageIndex(int x, int y)
    {
        return y * inputSize + x;
    }

    protected final int getKernelX(int n)
    {
        return n % Constants.KERNEL_SIZE;
    }

    protected final int getKernelY(int n)
    {
        return n / Constants.KERNEL_SIZE;
    }

    protected final int getFeatureMapX(int n)
    {
        return n % featureMapSize;
    }

    protected final int getFeatureMapY(int n)
    {
        return n / featureMapSize;
    }

}
