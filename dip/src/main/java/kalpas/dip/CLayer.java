package kalpas.dip;

import java.util.Arrays;
import java.util.Random;

import kalpas.dip.general.Constants;

public class CLayer
{
    public double[][] featureMaps;     // outputs
    public int         featureMapCount;
    public int         featureMapSize;
    private int        inputSize;
    private int        neurons;//in feature map
    private double[][] dErrorDy;
    private double[][][] dErrorDw;
    //private double[][] dErrorDxm1;
    private double[][] kernelWeights;

    // ///////////////

    private double[]   input;

    private int        inputs;

    private int        BIAS;

    public CLayer(int featureMapSize, int featureMapCount, int inputSize)
    {
        final int fmapElCount = featureMapSize * featureMapSize;

        // //////////
       /* this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        kernelWeights = new double[featureMapCount][Constants.KERNEL_SIZE
                * Constants.KERNEL_SIZE + 1];
        featureMaps = new double[featureMapCount][fmapElCount];
        bias = new double[featureMapCount][fmapElCount];
        initKernelWeights();
        initBias();
*/
        // //////////

        this.BIAS = Constants.KERNEL_ELEMENTS;

        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        this.neurons = featureMapSize * featureMapSize;

        kernelWeights = new double[featureMapCount][Constants.KERNEL_ELEMENTS + 1];
        featureMaps = new double[featureMapCount][fmapElCount];
        dErrorDy = new double[featureMapCount][fmapElCount];
        dErrorDw = new double[featureMapCount][fmapElCount][Constants.KERNEL_ELEMENTS + 1];
        //dErrorDxm1 = new double[featureMapCount][fmapElCount];

        initKernelWeights();

    }

    private double activationFunction(double n)
    {
        return Math.tanh(n);
    }
    
    private double activationFunctionDerivative(int feature, int index)
    {
        return 1 - Math.pow(featureMaps[feature][index], 2);
    }

    public double[][] process(byte[] image)
    {
        this.input = new double[image.length];
        for(int i = 0 ; i < image.length; i++)
        {
            this.input[i] = (double)(image[i] & 0xFF);
        }
        
        double sum = 0;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuron = 0; neuron < neurons; neuron++)
            {
                sum += kernelWeights[feature][BIAS];
                for(int kernelElement = 0; kernelElement < Constants.KERNEL_ELEMENTS; kernelElement++)
                {
                    // TODO optimize
                    sum += this.input[getImageIndex(getKernelX(kernelElement)
                            + getFeatureMapX(neuron), getKernelY(kernelElement)
                            + getFeatureMapY(neuron))]
                            * kernelWeights[feature][kernelElement];
                }
                featureMaps[feature][neuron] = activationFunction(sum);
                sum = 0;
            }
        }
        return featureMaps;
    }

    public void backPropagate(double[][] dErrorDx)
    {
        double dEdY;
//        Arrays.fill(dErrorDxm1, 0.0);
        for(double[][] array : dErrorDw)
            for(double[] subArray: array)
                Arrays.fill(subArray, 0.0);
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
            {
                dEdY = activationFunctionDerivative(feature,neuronIndex)
                        * dErrorDx[feature][neuronIndex];
                dErrorDy[feature][neuronIndex] = dEdY;
                dErrorDw[feature][neuronIndex][BIAS] = dEdY;
                for(int connectIndex = 0; connectIndex < Constants.KERNEL_ELEMENTS; connectIndex++)
                {
                    dErrorDw[feature][neuronIndex][connectIndex] += dEdY
                            * input[connectIndex];
//                    dErrorDxm1[connectIndex] += dEdY
//                            * kernelWeights[neuronIndex][connectIndex];
                }
    
            }
        }
        
        //------------

        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
            {
                for(int connectIndex = 0; connectIndex < Constants.KERNEL_ELEMENTS; connectIndex++)
                {
                    kernelWeights[feature][connectIndex] -= Constants.ETA_LEARNIG_RATE
                            * dErrorDw[feature][neuronIndex][connectIndex];
                }
            }
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

    private final void initKernelWeights()
    {
        Random randomizer = new Random();
        final double pow = Math.pow(Constants.KERNEL_ELEMENTS, -0.5);
        for(double[] array : kernelWeights)
            for(int i = 0; i < Constants.KERNEL_ELEMENTS; i++)
                array[i] = randomizer.nextGaussian() * pow;
    }

}
