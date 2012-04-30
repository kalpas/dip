package kalpas.dip;


public interface NeuralNetwork
{
    public int process(Image image);
    
    public void backPropagate();
    
    public double[] getdErrorDx();
    
    public boolean isFault();
}
