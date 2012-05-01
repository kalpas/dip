package kalpas.dip;

import java.io.IOException;

import kalpas.dip.general.Trainer;
import kalpas.dip.simple.SimpleNetwork;

/**
 * Hello world!
 * 
 */
public class App
{
    private static TrainingSet trainingSet;
    private static TestSet     testSet;
    private static double[]    dErrorDx;

    static
    {
        trainingSet = new TrainingSet();
        testSet = new TestSet();
        dErrorDx = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
            0.0 };

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        System.out.println("Start");
        
       /* FileInputStream fis = new FileInputStream("SimpleNetwork");
        ObjectInputStream oin = new ObjectInputStream(fis);
        SimpleNetwork net = (SimpleNetwork)oin.readObject();*/
        
        
        System.out.println("Loaded");
        
        Trainer simpleNetworkTrainer = Trainer.train(new SimpleNetwork()).onTrainSet();
        simpleNetworkTrainer.start(1);
        System.out.println("Trained");
        simpleNetworkTrainer.test();
        System.out.println("Tested");
        System.out.println("Finished");
        
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
