package kalpas.dip;

import java.util.Random;

import kalpas.dip.general.Constants;

public class CLayer
{
    private double[] input;
    private double[][] weights;
    private double[][] featureMaps;
    
    private double[][]   dErrorDy;
    private double[][] dErrorDw;
    private double[][] dErrorDxm1;

    public int         featureMapCount;
    public int         featureMapSize;
    
    private int        inputSize;


    public CLayer(int featureMapSize, int featureMapCount, int inputSize)
    {
        final int neuronNumber = featureMapSize*featureMapSize;
        
        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        weights = new double[featureMapCount][Constants.KERNEL_ELEMENTS + 1];
        featureMaps = new double[featureMapCount][neuronNumber];
        dErrorDy = new double[featureMapCount][neuronNumber];
        dErrorDw = new double[featureMapCount][Constants.KERNEL_ELEMENTS + 1];
        //dErrorDxm1 = new double[neurons][];
        initKernelWeights();

    }

    private double activationFunction(double n)
    {
        return 1.71*Math.tanh(0.6666666*n);
    }

    public double[][] process(byte[] image)
    {
        /*int neuronCount = featureMapSize * featureMapSize;
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
        return featureMaps;*/
        return null;
    }

    private final void initKernelWeights()
    {
        Random randomizer = new Random();
        final double pow = Math.pow(Constants.KERNEL_ELEMENTS, -0.5);
        for(double[] array: weights)
            for(int i = 0; i < Constants.KERNEL_ELEMENTS;i++)
                array[i] = randomizer.nextGaussian()*pow;
    }
    

}
