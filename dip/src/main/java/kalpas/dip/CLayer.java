package kalpas.dip;

public class CLayer implements Layer
{
    public final int KERNEL_SIZE = 5;
    
    private int[][]  weights;
    
    private int[][] featureMaps;

    public CLayer(int featureMapSize, int featureMapCount)
    {
        weights = new int[featureMapCount][KERNEL_SIZE * KERNEL_SIZE + 1];
        featureMaps = new int[featureMapCount][featureMapSize * featureMapSize];
    }

    /* (non-Javadoc)
     * @see kalpas.dip.Layer#process(byte[])
     */
    public void process(byte[] image)
    {

    }

}
