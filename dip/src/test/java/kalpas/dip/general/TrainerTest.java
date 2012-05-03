package kalpas.dip.general;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import kalpas.dip.simple.SimpleNetwork;

import org.junit.Ignore;
import org.junit.Test;

public class TrainerTest
{
    @Ignore
    @Test
    public void trainerTest() throws IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream("SimpleNetwork");
        ObjectInputStream oin = new ObjectInputStream(fis);
        SimpleNetwork net = (SimpleNetwork)oin.readObject();
        
        
        
        Trainer simpleNetworkTrainer = Trainer.train(net).onTrainSet();
        simpleNetworkTrainer.reinit();
        simpleNetworkTrainer.test();
        simpleNetworkTrainer.viewMSE(simpleNetworkTrainer.getTestMSE());
//        simpleNetworkTrainer.start(1);
//        simpleNetworkTrainer.test();
//        simpleNetworkTrainer.dump(1);
    }

}
