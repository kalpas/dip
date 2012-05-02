package kalpas.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import kalpas.dip.app.AppCmd;

public class App
{
    public static void main(String[] args)
    {
        final String fin = "fin";

        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String line = "";
        StringTokenizer tokens = null;
        String token = null;
        String[] arguemens;
        boolean argsAreValid = false;
        AppCmd cmd = null;

        while(!fin.equals(line))
        {
            try
            {
                line = in.readLine();
            }
            catch(IOException e)
            {
                System.err.println("smth bad: " + e.getCause());
                e.printStackTrace();
            }
            if(line != null)
            {
                tokens = new StringTokenizer(line);
                if(tokens.hasMoreTokens())
                {
                    token = tokens.nextToken();
                    if(!fin.equals(token))
                    {
                        try
                        {
                            cmd = AppCmd.valueOf(token);
                        }
                        catch(IllegalArgumentException e)
                        {
                        }
                        if(cmd != null)
                        {
                            argsAreValid = true;
                            arguemens = new String[cmd.getArgNumber()];
                            for(int i = 0; i < arguemens.length; i++)
                            {
                                if(tokens.hasMoreTokens())
                                    arguemens[i] = tokens.nextToken();
                                else
                                {
                                    argsAreValid = false;
                                    break;
                                }
                            }
                            if(argsAreValid)
                            {
                                if(cmd.execute(arguemens))
                                {
                                    System.out.println(cmd.toString()+ " executed successfully");
                                }
                                else
                                {
                                    System.err.println("errors while executing "+cmd.toString());
                                }
                                cmd = null;
                            }
                            else
                            {
                                System.err.println("wrong number of params");
                            }
                        }
                        else
                        {
                            System.err.println("command doesn't exist");
                        }
                    }
                    else
                    {
                        System.out.println("Exiting. FIN");
                        return;
                    }
                }
                else
                {
                    System.out.println("please type a command");
                }
            }

        }

        // System.out.println("Start");
        //
        // if(true)
        // {
        // Constants.ETA_LEARNIG_RATE = 0.0007;
        //
        // FileInputStream fis = new FileInputStream(
        // "dump\\1335932552421\\kalpas.dip.general.Trainer");
        // ObjectInputStream oin = new ObjectInputStream(fis);
        // Trainer trainer = (Trainer) oin.readObject();
        // trainer.reinit();
        //
        // trainer.test();
        //
        // // NeuralNet net = new NeuralNet();
        // // net.layer1 = ((NeuralNet)(trainer.getNet())).layer1;
        // // trainer = Trainer.train(net).onTrainSet();
        //
        // // trainer.test();
        // // trainer.start(2);
        // // trainer.test();
        //
        // /* NeuralNet net = new NeuralNet();
        // net.layer1 = ((SimpleNetwork)(trainer.getNet())).layer1;
        //
        //
        // Trainer neuralNet = Trainer.train(net).onTrainSet();
        // neuralNet.save("WIP");*/
        //
        // // System.out.println("Loaded");
        // // trainer.reinit();
        // // trainer.test();
        // // trainer.viewNetwork(999, true);
        // }
        // else
        // {
        // Constants.ETA_LEARNIG_RATE = 0.001;
        //
        // /*FileInputStream fis = new FileInputStream(
        // "dump\\1335932552421\\kalpas.dip.general.Trainer");
        // ObjectInputStream oin = new ObjectInputStream(fis);
        // Trainer trainer = (Trainer) oin.readObject();
        //
        // System.out.println("Loaded");
        // trainer.reinit();
        // trainer.setDir(new File("1"));
        //
        // trainer.viewNetwork(1, true);
        //
        // trainer.test();
        // trainer.onTestSet();
        // */
        //
        // Trainer trainer = Trainer.train(new SimpleNetwork()).onTrainSet();
        // trainer.viewNetwork(1, true);
        //
        // trainer.start(5);
        // System.out.println("Trained");
        // trainer.test();
        // System.out.println("Tested");
        // System.out.println("Finished");
        // trainer.viewNetwork(7777, true);
        // }
        //
        // /*System.out.println("Hello neural network world!");
        //
        // JFrame frame = new JFrame("Frame in Java Swing");
        // frame.setSize(400, 800);
        // Visualize visualize = new Visualize();
        // frame.getContentPane().add(visualize);
        // frame.setVisible(true);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        // CLayer layer1 = new CLayer(24, 7, trainingSet.columns);
        // Flayer layer2 = new Flayer(10, 4032);
        //
        // Image image = null;
        // for(int i = 0; i < 10; i++)
        // {
        // trainingSet = new TrainingSet();
        // double[] output = null;
        // for(int j = 0; j < trainingSet.imageCount; j++)
        // {
        // image = trainingSet.getNextImage();
        // output = layer2.process(layer1.process(image.bytes));
        // getError(output, image.value);
        // layer1.backPropagate(layer2.backPropagate(dErrorDx));
        // //
        // System.out.println(layer2.process(process(layer1.process(image.bytes))));
        // // layer1.backPropagate(layer2.backPropagate(new double[]{}));
        // }
        // Constants.ETA_LEARNIG_RATE/=100;
        // }
        //
        // Visualize.drawInput(image.bytes, trainingSet.columns);
        // Visualize.drawKernels(layer1.getKernelWeights());
        // Visualize.draw1Layer(layer1.featureMaps, layer1.featureMapCount,
        // layer1.featureMapSize);
        //
        // // frame.setVisible(false);
        // // frame.dispose();
        //
        // visualize.repaint();
        //
        // FileOutputStream fos = new FileOutputStream("temp.out");
        // ObjectOutputStream oos = new ObjectOutputStream(fos);
        // oos.writeObject(layer1);
        // oos.writeObject(layer2);
        // oos.flush();
        // oos.close();*/

    }
}
