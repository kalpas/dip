package kalpas.dip.complicated;

import java.io.Serializable;
import java.util.Arrays;

import javax.swing.JFrame;

import kalpas.dip.CLayer;
import kalpas.dip.CLayer2;
import kalpas.dip.Flayer;
import kalpas.dip.Image;
import kalpas.dip.NeuralNetwork;
import kalpas.dip.Visualize;

public class NeuralNet implements NeuralNetwork, Serializable
{
    private static final long serialVersionUID = 1L;

    private double[]          output;
    private double[]          dErrorDx;
    private boolean           fault;

    public CLayer             layer1;
    public CLayer2            layer2;
    public Flayer             layer3;
    public Flayer             layer4;

    public NeuralNet()
    {
        dErrorDx = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0 };
        layer1 = new CLayer(24, 6, 28);
        layer2 = new CLayer2(11, 50, layer1.featureMapCount, layer1.featureMapSize);
        layer3 = new Flayer(100, layer2.featureMapSize * layer2.featureMapSize
                * layer2.featureMapCount);
        layer4 = new Flayer(10, layer3.neurons);
    }

    public int process(Image image)
    {
        fault = true;
        output = layer4.process(layer3.process(layer2.process(layer1.process(image.bytes))));
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
        if(maxIndex == image.value)
            fault = false;
        getError(image.value);
        return maxIndex;
    }

    public void backPropagate()
    {
        if(output != null)
        {
            layer1.backPropagate(layer2.backPropagate(layer3.backPropagate(layer4.backPropagate(dErrorDx))));

        }
    }

    private void getError(int n)
    {
        Arrays.fill(dErrorDx, -1.0);
        // Arrays.fill(dErrorDx, -1.0*Constants.scaleY);
        dErrorDx[n] = 1.0;
        // dErrorDx[n] = 1.0*Constants.scaleY;
        for(int i = 0; i < 10; i++)
        {
            dErrorDx[i] = output[i] - dErrorDx[i];
        }
    }
    
    public double getMSE()
    {
        double MSE= 0;
        for(double value: dErrorDx)
        {
            MSE +=value*value;
        }
        return 0.5*MSE;
    }
    
    public void viewNetwork(Image image)
    {
        JFrame frame = new JFrame("network");
        frame.setSize(1200, 900);
        Visualize visualize = new Visualize();
        frame.getContentPane().add(visualize);
        frame.setVisible(true);

        this.process(image);

        int imageSize = (int)Math.sqrt(image.bytes.length);
        Visualize.drawInput(image.bytes,imageSize);
        Visualize.drawKernels(layer1.getKernelWeights());
        Visualize.draw1Layer(layer1.featureMaps, layer1.featureMapCount,
                layer1.featureMapSize);
        Visualize.draw2Kernels(layer2.getKernelWeights(), 4);
        Visualize.draw2Layer(layer2.featureMaps, layer2.featureMapCount, layer2.featureMapSize);
        Visualize.draw3Layer(layer3.getOutput());
        Visualize.drawOutput(layer4.getOutput());

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

    /**
     * @return the layer1
     */
    public CLayer getLayer1()
    {
        return layer1;
    }

    /**
     * @param layer1 the layer1 to set
     */
    public void setLayer1(CLayer layer1)
    {
        this.layer1 = layer1;
    }

    /**
     * @return the layer2
     */
    public CLayer2 getLayer2()
    {
        return layer2;
    }

    /**
     * @param layer2 the layer2 to set
     */
    public void setLayer2(CLayer2 layer2)
    {
        this.layer2 = layer2;
    }

    /**
     * @return the layer3
     */
    public Flayer getLayer3()
    {
        return layer3;
    }

    /**
     * @param layer3 the layer3 to set
     */
    public void setLayer3(Flayer layer3)
    {
        this.layer3 = layer3;
    }

    /**
     * @return the layer4
     */
    public Flayer getLayer4()
    {
        return layer4;
    }

    /**
     * @param layer4 the layer4 to set
     */
    public void setLayer4(Flayer layer4)
    {
        this.layer4 = layer4;
    }

}
