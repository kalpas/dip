package kalpas.dip;

public class Image
{
    public Image(byte[] bytes, int value, int index)
    {
        this.bytes = bytes;
        this.value = value;
        this.index = index;
        
    }
    
    public byte[] bytes;
    
    public int value;
    
    public int index;
}
