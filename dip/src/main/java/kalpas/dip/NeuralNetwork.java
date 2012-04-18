package kalpas.dip;


public class NeuralNetwork
{
    private Layer[] layers;

    public NeuralNetwork()
    {
        layers = new Layer[4];
        Layer newLayer = new CLayer(13, 6);
        layers[0] = newLayer;
        newLayer = new CLayer(50, 5);
        layers[1] = newLayer;


    }

    public int process(byte[] image)
    {
        return 0;
    }

}
