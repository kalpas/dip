package kalpas.dip;

import java.io.IOException;

public class TrainingSet extends DataSet
{

    public TrainingSet() throws IOException
    {
        super();
    }

    @Override
    protected String getLabelFile()
    {
        return "train-labels.idx1-ubyte";
    }

    @Override
    protected String getImageFile()
    {
        return "train-images.idx3-ubyte";
    }

}
