package kalpas.dip;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;

@Deprecated
public class MNISTdao
{
    private FileInputStream              fileInputStream;
    private BufferedInputStream          bufferedInputStream;
    int                                  numberOfItems;
    HashMap<String, BufferedInputStream> streams           = new HashMap<String, BufferedInputStream>();

    final String                         TRAIN_LABELS_FILE = "train-labels.idx1-ubyte";
    final String                         TRAIN_IMAGES_FILE = "train-images.idx3-ubyte";
    final String                         TEST_LABELS_FILE  = "t10k-labels.idx1-ubyte";
    final String                         TEST_IMAGES_FILE  = "t10k-images.idx3-ubyte";

    @Deprecated
    public MNISTdao()
    {
        prepare();
    }

    private final void prepare()
    {
        String[] resources = { TRAIN_LABELS_FILE, TRAIN_IMAGES_FILE,
                TEST_LABELS_FILE, TEST_IMAGES_FILE };

        byte[] buffer;

        for(String resource : resources)
        {
            try
            {
                URL trainLabels = this.getClass().getResource(resource);
                fileInputStream = new FileInputStream(trainLabels.getFile());
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                System.out.println(resource);
                streams.put(resource, bufferedInputStream);

            }
            catch(FileNotFoundException e)
            {
                System.err.println("smth bad happened\n" + e.getStackTrace());
            }

            try
            {
                if(bufferedInputStream.available() > 0)
                {
                    buffer = new byte[4];
                    bufferedInputStream.read(buffer);
                    System.out.println("Magic number: "
                            + ByteBuffer.wrap(buffer).getInt());
                    bufferedInputStream.read(buffer);
                    numberOfItems = ByteBuffer.wrap(buffer).getInt();
                    System.out.println("Number of items: " + numberOfItems);
                }
            }
            catch(IOException e)
            {
                System.err.println("smth bad happened\n" + e.getStackTrace());
            }
        }

    }

    public void readTrainingPair()
    {

    }

    private byte readTrainLabel()
    {
        BufferedInputStream stream = streams.get(TRAIN_LABELS_FILE);
        try
        {
            return (byte) stream.read();
        }
        catch(IOException e)
        {
            System.err.println("smth bad happened\n" + e.getStackTrace());
        }
        return -1;
    }
}
    
