package kalpas.dip.simple;

import java.io.Serializable;
import java.util.Arrays;

import kalpas.dip.CLayer;
import kalpas.dip.Flayer;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;

public class SimpleNetwork implements NeuralNetwork, Serializable
{
    private static final long serialVersionUID = 1L;
    
    private double[] output;
    private double[] dErrorDx;
    private boolean fault;

    private CLayer   layer1;
    private Flayer   layer2;
    

    public SimpleNetwork()
    {
        dErrorDx = new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        layer1 = new CLayer(24, 6, 28);
        layer2 = new Flayer(10, layer1.featureMapSize*layer1.featureMapSize*layer1.featureMapCount);
    }
    
    public int process(Image image)
    {
        fault = true;
        output = layer2.process(layer1.process(image.bytes));
        double max = -1.0;
        int maxIndex = 0;
        for(int i = 0; i < 10; i++)
        {
            if(output[i] > max)
            {
                max = output[i];
                maxIndex = i;
            }
        }
        if(maxIndex==image.value)
            fault = false;
        getError(image.value);
        return maxIndex;
    }

    public void backPropagate()
    {
        if(output != null)
        {
            layer1.backPropagate(layer2.backPropagate(dErrorDx));

        }
    }

    private void getError(int n)
    {
        Arrays.fill(dErrorDx, -1.0);
        dErrorDx[n] = 1.0;
        for(int i = 0; i < 10; i++)
        {
            dErrorDx[i] = output[i] - dErrorDx[i];
        }
    }

    /**
     * @param layer1 the layer1 to set
     */
    public void setLayer1(CLayer layer1)
    {
        this.layer1 = layer1;
    }

    /**
     * @param layer2 the layer2 to set
     */
    public void setLayer2(Flayer layer2)
    {
        this.layer2 = layer2;
    }

    /**
     * @return the dErrorDx
     */
    public double[] getdErrorDx()
    {
        return dErrorDx;
    }

    /**
     * @return the fault
     */
    public boolean isFault()
    {
        return fault;
    }


}
