package kalpas.dip;


public class TrainingSet extends DataSet
{

    public TrainingSet()
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
