package kalpas.dip.app.neural.networks;

import kalpas.dip.app.data.Image;


public interface NeuralNetwork
{
    public int process(Image image);
    
    public void backPropagate();
    
    public double[] getdErrorDx();
    
    public boolean isFault();
    
    public double getMSE();
    
    public void viewNetwork(Image image);
}
