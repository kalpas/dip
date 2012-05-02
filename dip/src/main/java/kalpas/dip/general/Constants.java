package kalpas.dip.general;

import java.util.HashMap;
import java.util.Map;

public class Constants
{

    public static final int KERNEL_SIZE = 5;
    
    public static final int KERNEL_SIZE2 = 4;

    public static final int KERNEL_ELEMENTS = KERNEL_SIZE * KERNEL_SIZE;
    
    public static final int KERNEL_ELEMENTS2 = KERNEL_SIZE2 * KERNEL_SIZE2;
    
    public static double ETA_LEARNIG_RATE = 0.0005;
    
    //public static double scaleY = 1.7159;
    
    //public static double scaleX = 0.66666667;
    
    private static Map<Double,Double> cache = new HashMap<Double, Double>();
            
    private static Double value;
    
    @Deprecated
    public static double tanh(double arg)
    {
        value = cache.get(arg);
        if(value!=null)
            return value;
        else
        {
            value = Math.tanh(arg);
            cache.put(arg, value);
            return value;
        }
    }

}
