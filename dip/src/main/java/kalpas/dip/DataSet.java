package kalpas.dip;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public class DataSet
{
    public int                  labelsCount;
    public int                  imageCount;
    public int                  rows;
    public int                  columns;

    private final int           LABELS            = 2049;
    private final int           IMAGES            = 2051;

    private FileInputStream     fileInputStream;
    private BufferedInputStream labelsBufferedStream;
    private BufferedInputStream imagesBufferedStream;

    
    protected String getLabelFile()
    {
        return null;
    }
    
    protected String getImageFile()
    {
        return null;
    }

    public DataSet() throws IOException
    {
        prepare();
    }

    private void prepare() throws IOException
    {
        byte[] buffer;

        try
        {
            URL url = this.getClass().getResource(getLabelFile());
            fileInputStream = new FileInputStream(url.getFile());
            labelsBufferedStream = new BufferedInputStream(fileInputStream);
            url = this.getClass().getResource(getImageFile());
            fileInputStream = new FileInputStream(url.getFile());
            imagesBufferedStream = new BufferedInputStream(fileInputStream);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("smth bad happened\n" + e.getStackTrace());
        }

        if(labelsBufferedStream.available() > 0)
        {
            buffer = new byte[4];
            labelsBufferedStream.read(buffer);
            if(ByteBuffer.wrap(buffer).getInt() != LABELS)
                System.err.println("It's no a file with labels");
            labelsBufferedStream.read(buffer);
            labelsCount = ByteBuffer.wrap(buffer).getInt();
            if(labelsCount < 0)
                System.err.println("smth wrong. labels count is: "
                        + labelsCount);

            // ---------------

            imagesBufferedStream.read(buffer);
            if(ByteBuffer.wrap(buffer).getInt() != IMAGES)
                System.err.println("It's no a file with images");
            imagesBufferedStream.read(buffer);
            imageCount = ByteBuffer.wrap(buffer).getInt();
            if(imageCount < 0)
                System.err
                        .println("smth wrong. images count is: " + imageCount);
            imagesBufferedStream.read(buffer);
            rows = ByteBuffer.wrap(buffer).getInt();
            imagesBufferedStream.read(buffer);
            columns = ByteBuffer.wrap(buffer).getInt();

        }
        if(labelsCount != imageCount)
            System.err
                    .println("Smth went wrong. Labels and Images count are different");

    }

    public byte[] getImage() throws IOException
    {
        byte[] image = new byte[columns * rows];
        imagesBufferedStream.read(image);
        return image;
    }

    public int getLabel() throws IOException
    {
        return labelsBufferedStream.read();
    }

    @Override
    public String toString()
    {
        return imageCount + " images " + rows + "x" + columns;
    }

}
