package kalpas.dip;

import java.io.IOException;

public class TestSet extends DataSet
{

    public TestSet() throws IOException
    {
        super();
    }

    @Override
    protected String getLabelFile()
    {
        return "t10k-labels.idx1-ubyte";
    }

    @Override
    protected String getImageFile()
    {
        return "t10k-images.idx3-ubyte";
    }

}
