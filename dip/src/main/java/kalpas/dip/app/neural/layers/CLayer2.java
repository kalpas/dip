package kalpas.dip.app.neural.layers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import kalpas.dip.app.neural.Core;

public class CLayer2 implements Serializable
{
    private static final long serialVersionUID = 1L;

    public double[]           featureMaps;          // outputs

    public int                featureMapCount;
    public int                featureMapSize;
    private int               inputMaps;
    private int               inputSize;
    private int               neurons;              // in feature map

    private double[][]        dErrorDy;
    private double[][][][]    dErrorDw;
    private double[]          dErrorDxm1;

    private double[][][]      kernelWeights;
    private int               BIAS;

    private double[]          input;

    // final double scaleY = Constants.scaleY;
    // final double scaleX = Constants.scaleX;

    public CLayer2(int featureMapSize, int featureMapCount, int inputMaps,
            int inputSize)
    {

        this.BIAS = Core.KERNEL_ELEMENTS2;

        this.featureMapCount = featureMapCount;
        this.featureMapSize = featureMapSize;
        this.inputSize = inputSize;
        this.neurons = featureMapSize * featureMapSize;
        this.inputMaps = inputMaps;

        kernelWeights = new double[featureMapCount][inputMaps][Core.KERNEL_ELEMENTS2 + 1];
        featureMaps = new double[featureMapCount * neurons];
        dErrorDy = new double[featureMapCount][neurons];
        dErrorDw = new double[featureMapCount][neurons][inputMaps][Core.KERNEL_ELEMENTS2 + 1];
        dErrorDxm1 = new double[inputMaps*inputSize*inputSize];
        // dErrorDxm1 = new double[featureMapCount][fmapElCount];

        initKernelWeights();

    }

    public double[] process(double[] input)
    {
        final int kernelSize = Core.KERNEL_SIZE2;
        final int sourceMapSize = inputSize * inputSize;

        this.input = input;

        double sum = 0;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronRow = 0; neuronRow < featureMapSize; neuronRow++)
            {
                for(int neuronCol = 0; neuronCol < featureMapSize; neuronCol++)
                {
                    for(int sourceFeture = 0; sourceFeture < inputMaps; sourceFeture++)
                    {
                        sum += kernelWeights[feature][sourceFeture][BIAS];
                        for(int kernelRow = 0; kernelRow < kernelSize; kernelRow++)
                        {
                            for(int kernelCol = 0; kernelCol < kernelSize; kernelCol++)
                            {
                                sum += this.input[(sourceFeture * sourceMapSize + (neuronRow*2 + kernelRow)
                                        * inputSize)
                                        + (neuronCol*2 + kernelCol)]
                                        * kernelWeights[feature][sourceFeture][kernelRow
                                                * kernelSize + kernelCol];
                            }
                        }
                    }
                    featureMaps[feature * neurons
                            + (neuronRow * featureMapSize + neuronCol)] = Math
                            .tanh(sum);
                    // featureMaps[feature * neurons
                    // + (neuronRow * featureMapSize + neuronCol)] =
                    // scaleY*Math.tanh(scaleX*sum);
                    sum = 0;
                }
            }
        }
        return featureMaps;
    }

    public double[] backPropagate(double[] dErrorDx)
    {
        final int sourceMapSize = inputSize * inputSize;
        final int kernelSize = Core.KERNEL_SIZE2;
        
        double dEdY;
        double outputValue;
        Arrays.fill(dErrorDxm1, 0.0);
        // Arrays.fill(dErrorDxm1, 0.0);
        /*  for(double[][] array : dErrorDw)
              for(double[] subArray : array)
                  Arrays.fill(subArray, 0.0);*/
        final int kernelElements = Core.KERNEL_ELEMENTS2;
        for(int feature = 0; feature < featureMapCount; feature++)
        {
//            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
//            {
            for(int neuronRow = 0; neuronRow < featureMapSize; neuronRow++)
            {
                for(int neuronCol = 0; neuronCol < featureMapSize; neuronCol++)
                {
                outputValue = featureMaps[feature * neurons + neuronRow*featureMapSize+neuronCol];
                dEdY = 1 - outputValue * outputValue;
                dEdY *= dErrorDx[feature * neurons + neuronRow*featureMapSize+neuronCol];
                dErrorDy[feature][neuronRow*featureMapSize+neuronCol] = dEdY;

                for(int sourceFeture = 0; sourceFeture < inputMaps; sourceFeture++)
                    {
                        dErrorDw[feature][neuronRow*featureMapSize+neuronCol][sourceFeture][BIAS] = dEdY;
                        
    //                    for(int connectIndex = 0; connectIndex < kernelElements; connectIndex++)
    //                    {
                        
                        for(int kernelRow = 0; kernelRow < kernelSize; kernelRow++)
                        {
                            for(int kernelCol = 0; kernelCol < kernelSize; kernelCol++)
                            {
                            dErrorDw[feature][neuronRow*featureMapSize+neuronCol][sourceFeture][kernelRow*kernelSize+kernelCol] = dEdY
                                    * input[neuronRow*featureMapSize+neuronCol];
    
                            dErrorDxm1[(sourceFeture * sourceMapSize + (neuronRow*2 + kernelRow)
                                       * inputSize)
                                       + (neuronCol*2 + kernelCol)] += dEdY
                             * kernelWeights[feature][sourceFeture][kernelRow*kernelSize+kernelCol];
                            }
                        }
    
                    }
                }
            }
        }

        final double eta = Core.ETA_LEARNIG_RATE;
        
        for(int feature = 0; feature < featureMapCount; feature++)
        {
            for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
            {
                for(int sourceFeture = 0; sourceFeture < inputMaps; sourceFeture++)
                {
                    kernelWeights[feature][sourceFeture][BIAS] -= eta
                            * dErrorDw[feature][neuronIndex][sourceFeture][BIAS];
                    for(int connectIndex = 0; connectIndex < kernelElements; connectIndex++)
                    {
                        kernelWeights[feature][sourceFeture][connectIndex] -= eta
                                * dErrorDw[feature][neuronIndex][sourceFeture][connectIndex];
                    }
                }
            }
        }
        
        return dErrorDxm1;

    }

    private final void initKernelWeights()
    {
        Random randomizer = new Random();
        final double pow = Math
                .pow(Core.KERNEL_ELEMENTS2 * inputMaps, -0.5);
        for(double[][] array : kernelWeights)
            for(int s = 0; s < inputMaps; s++)
                for(int i = 0; i < Core.KERNEL_ELEMENTS2; i++)
                    array[s][i] = randomizer.nextGaussian() * pow;
    }

    /**
     * @return the kernelWeights
     */
    public double[][][] getKernelWeights()
    {
        return kernelWeights;
    }

}
