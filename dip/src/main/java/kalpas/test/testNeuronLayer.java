package kalpas.test;

public class testNeuronLayer
{
    private double[]   dErrorDy;
    private double[][] dErrorDw;
    
    private double[] input;
    private double[][] weights;
    private double[]   output;
    
    private int        neurons;
    private int        inputs;
    
    private int BIAS;

    public testNeuronLayer(int neurons, int inputs)
    {
        this.BIAS =inputs;
        this.neurons = neurons;
        this.inputs = inputs;
        weights = new double[neurons][inputs+1];
        output = new double[neurons];
        dErrorDy = new double[neurons];
        dErrorDw = new double[neurons][inputs+1];
        
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
        
        
        for(int neuronIndex = 0; neuronIndex < neurons; neuronIndex++)
        {
            final double dEdY = activationFunctionDerivative(neuronIndex)* dErrorDx[neuronIndex];
            dErrorDy[neuronIndex] = dEdY;
            for(int connectIndex = 0; connectIndex < inputs; connectIndex++)
            {
                dErrorDw[neuronIndex][connectIndex] = dEdY*input[connectIndex];
            }
        }
        
        
        
        
        
        double errorDerivative;// xi - ti (actual - target)
        
        double activationDerivative;//1-x^2
        
        return null;

    }
    
    private double activationFunction(double value)
    {
        return Math.tanh(value);
    }
    
    private double activationFunctionDerivative(int index)
    {
        return 1 - Math.pow(output[index], 2);
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
