package kalpas.dip.app.data;



public class TestSet extends DataSet
{

    public TestSet()
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
