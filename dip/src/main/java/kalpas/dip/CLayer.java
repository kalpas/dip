package kalpas.dip;

import java.io.Serializable;
import java.util.Random;

import kalpas.dip.general.Constants;

public class CLayer implements Serializable
{
    private static final long serialVersionUID = 1L;

    public double[]           featureMaps;          // outputs

    public int                featureMapCount;
    public int                featureMapSize;
    private int               inputSize;
    private int               neurons;              // in feature map

    private double[][]        dErrorDy;
    private double[][][]      dErrorDw;
    // private double[][] dErrorDxm1;

    private double[][]        kernelWeights;
    private int               BIAS;

    private double[]          input;
    
//    final double scaleY = Constants.scaleY;
//    final double scaleX = Constants.scaleX;

    public CLayer(int featureMapSize, int featureMapCount, int inputSize)
    {

        this.BIAS = Constants.KERNEL_ELEMENTS;

        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        this.neurons = featureMapSize * featureMapSize;

        kernelWeights = new double[featureMapCount][Constants.KERNEL_ELEMENTS + 1];
        featureMaps = new double[featureMapCount * neurons];
        dErrorDy = new double[featureMapCount][neurons];
        dErrorDw = new double[featureMapCount][neurons][Constants.KERNEL_ELEMENTS + 1];
        // dErrorDxm1 = new double[featureMapCount][fmapElCount];

        initKernelWeights();

    }

    public double[] process(byte[] image)
    {
        final int kernelSize = Constants.KERNEL_SIZE;
        
        this.input = new double[image.length];
        for(int i = 0; i < image.length; i++)
        {
            this.input[i] = (double) (image[i] & 0xFF);
        }

        double sum = 0;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronRow = 0; neuronRow < featureMapSize; neuronRow++)
            {
                for(int neuronCol = 0; neuronCol < featureMapSize; neuronCol++)
                {
                    sum += kernelWeights[feature][BIAS];
                    for(int kernelRow = 0; kernelRow < kernelSize; kernelRow++)
                    {
                        for(int kernelCol = 0; kernelCol < kernelSize; kernelCol++)
                        {
                            sum += this.input[(neuronRow + kernelRow)
                                    * inputSize + (neuronCol + kernelCol)]
                                    * kernelWeights[feature][kernelRow
                                            * kernelSize + kernelCol];
                        }
                    }
                    featureMaps[feature * neurons
                                + (neuronRow * featureMapSize + neuronCol)] = Math.tanh(sum);
//                    featureMaps[feature * neurons
//                            + (neuronRow * featureMapSize + neuronCol)] = scaleY*Math.tanh(scaleX*sum);
                    sum = 0;
                }
            }
        }
        return featureMaps;
    }

    public void backPropagate(double[] dErrorDx)
    {
        double dEdY;
        double outputValue;
        // Arrays.fill(dErrorDxm1, 0.0);
      /*  for(double[][] array : dErrorDw)
            for(double[] subArray : array)
                Arrays.fill(subArray, 0.0);*/
        final int kernelElements = Constants.KERNEL_ELEMENTS;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
            {
               
                outputValue = featureMaps[feature * neurons + neuronIndex];
                dEdY =  1 - outputValue*outputValue;
                //dEdY =  scaleX/scaleY*(scaleY+outputValue)*(scaleY-outputValue);
                dEdY *= dErrorDx[feature * neurons + neuronIndex];
                dErrorDy[feature][neuronIndex] = dEdY;
                dErrorDw[feature][neuronIndex][BIAS] = dEdY;
                for(int connectIndex = 0; connectIndex < kernelElements; connectIndex++)
                {
                    dErrorDw[feature][neuronIndex][connectIndex] = dEdY
                            * input[connectIndex];
                    // dErrorDxm1[connectIndex] += dEdY
                    // * kernelWeights[neuronIndex][connectIndex];
                }

            }
        }

        
        final double eta = Constants.ETA_LEARNIG_RATE;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
            {
                kernelWeights[feature][BIAS] -= eta * dErrorDw[feature][neuronIndex][BIAS];
                for(int connectIndex = 0; connectIndex < kernelElements; connectIndex++)
                {
                    kernelWeights[feature][connectIndex] -= eta
                            * dErrorDw[feature][neuronIndex][connectIndex];
                }
            }
        }

    }

    private final void initKernelWeights()
    {
        Random randomizer = new Random();
        final double pow = Math.pow(Constants.KERNEL_ELEMENTS, -0.5);
        for(double[] array : kernelWeights)
            for(int i = 0; i < Constants.KERNEL_ELEMENTS; i++)
                array[i] = randomizer.nextGaussian() * pow;
    }

    /**
     * @return the kernelWeights
     */
    public double[][] getKernelWeights()
    {
        return kernelWeights;
    }

}
