package kalpas.dip;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import kalpas.dip.general.Constants;
import kalpas.dip.general.Trainer;
import kalpas.dip.simple.SimpleNetwork;

/**
 * Hello world!
 * 
 */
public class App
{
    public static void main(String[] args) throws IOException,
            ClassNotFoundException
    {
        System.out.println("Start");

        if(true)
        {
            Constants.ETA_LEARNIG_RATE = 0.0007;
            
            FileInputStream fis = new FileInputStream(
                    "dump\\1335932552421\\kalpas.dip.general.Trainer");
            ObjectInputStream oin = new ObjectInputStream(fis);
            Trainer trainer = (Trainer) oin.readObject();
            trainer.reinit();
            
//            NeuralNet net = new NeuralNet();
//            net.layer1 = ((NeuralNet)(trainer.getNet())).layer1;
//            trainer = Trainer.train(net).onTrainSet();
            
            trainer.test();
            
            //trainer.start(2);
//            trainer.test();
            
           /* NeuralNet net = new NeuralNet();
            net.layer1 = ((SimpleNetwork)(trainer.getNet())).layer1;
            
            
            Trainer neuralNet = Trainer.train(net).onTrainSet();
            neuralNet.save("WIP");*/
            

//            System.out.println("Loaded");
//            trainer.reinit();
            //trainer.test();
            //trainer.viewNetwork(999, true);
        }
        else
        {
            Constants.ETA_LEARNIG_RATE = 0.001;
            
            /*FileInputStream fis = new FileInputStream(
                    "dump\\1335932552421\\kalpas.dip.general.Trainer");
            ObjectInputStream oin = new ObjectInputStream(fis);
            Trainer trainer = (Trainer) oin.readObject();

            System.out.println("Loaded");
            trainer.reinit();
            trainer.setDir(new File("1"));
            
            trainer.viewNetwork(1, true);
            
            trainer.test();
            trainer.onTestSet();
            */
            
            
            Trainer trainer = Trainer.train(new SimpleNetwork()).onTrainSet();
            trainer.viewNetwork(1, true);
            
            trainer.start(5);
            System.out.println("Trained");
            trainer.test();
            System.out.println("Tested");
            System.out.println("Finished");
            trainer.viewNetwork(7777, true);
        }

        /*System.out.println("Hello neural network world!");

        JFrame frame = new JFrame("Frame in Java Swing");
        frame.setSize(400, 800);
        Visualize visualize = new Visualize();
        frame.getContentPane().add(visualize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CLayer layer1 = new CLayer(24, 7, trainingSet.columns);
        Flayer layer2 = new Flayer(10, 4032);

        Image image = null;
        for(int i = 0; i < 10; i++)
        {
            trainingSet = new TrainingSet();
            double[] output = null;
            for(int j = 0; j < trainingSet.imageCount; j++)
            {
                image = trainingSet.getNextImage();
                output = layer2.process(layer1.process(image.bytes));
                getError(output, image.value);
                layer1.backPropagate(layer2.backPropagate(dErrorDx));
                // System.out.println(layer2.process(process(layer1.process(image.bytes))));
                // layer1.backPropagate(layer2.backPropagate(new double[]{}));
            }
            Constants.ETA_LEARNIG_RATE/=100;
        }

        Visualize.drawInput(image.bytes, trainingSet.columns);
        Visualize.drawKernels(layer1.getKernelWeights());
        Visualize.draw1Layer(layer1.featureMaps, layer1.featureMapCount,
                layer1.featureMapSize);

        // frame.setVisible(false);
        // frame.dispose();

        visualize.repaint();
        
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(layer1);
        oos.writeObject(layer2);
        oos.flush();
        oos.close();*/

    }
}
