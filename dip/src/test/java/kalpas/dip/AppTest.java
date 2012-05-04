package kalpas.dip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

import kalpas.dip.app.data.Image;
import kalpas.dip.app.data.TestSet;
import kalpas.dip.app.neural.layers.CLayer;
import kalpas.dip.app.neural.layers.Flayer;
import kalpas.dip.app.neural.networks.simple.SimpleNetwork;
import kalpas.dip.app.views.Visualize;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{

  
    @Test
    public void testApp() throws IOException, ClassNotFoundException
    {
        System.out.println("Hello neural network world!");

        JFrame frame = new JFrame("Frame in Java Swing");
        frame.setSize(400, 800);
        Visualize visualize = new Visualize();
        frame.getContentPane().add(visualize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FileInputStream fis = new FileInputStream("twoLayerConvolutionalNet");
        ObjectInputStream oin = new ObjectInputStream(fis);
        CLayer layer1 = (CLayer) oin.readObject();
        Flayer layer2 = (Flayer) oin.readObject();
        
        TestSet testSet = new TestSet();

        int errors = 0;
        Image image = null;
        double[] output = null;
        for(int j = 0; j < testSet.imageCount; j++)
        {
            image = testSet.getNextImage();
            output = layer2.process(layer1.process(image.bytes));
            double max = -1.0;
            int maxIndex = 0;
            for(int i = 0; i < 10; i++)
            {
                if(output[i]>max)
                {
                    max = output[i];
                    maxIndex = i;
                }
            }
            if(maxIndex!=image.value)
            {
                System.err.println("pattern not recognized: "+image.index);
                errors++;
            }
        }

        Visualize.drawInput(image.bytes, testSet.columns);
        Visualize.drawKernels(layer1.getKernelWeights());
        Visualize.draw1Layer(layer1.featureMaps, layer1.featureMapCount,
                layer1.featureMapSize);

        // frame.setVisible(false);
        // frame.dispose();

        visualize.repaint();
        System.out.println(errors +" - "+errors*100/testSet.imageCount+"%");
        
       
    }
    
    @Ignore
    @Test
    public void forSerPurpose() throws IOException, ClassNotFoundException
    {
        SimpleNetwork net = new SimpleNetwork();
        
        
        FileInputStream fis = new FileInputStream("twoLayerConvolutionalNet");
        ObjectInputStream oin = new ObjectInputStream(fis);
        CLayer layer1 = (CLayer) oin.readObject();
        Flayer layer2 = (Flayer) oin.readObject();
        
        net.setLayer1(layer1);
        net.setLayer2(layer2);
        
        FileOutputStream fos = new FileOutputStream("SimpleNetwork");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(net);
        oos.flush();
        oos.close();
        
    }

   

}
