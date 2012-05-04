package kalpas.dip.general;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Core
{

    public static final int            KERNEL_SIZE      = 5;

    public static final int            KERNEL_SIZE2     = 4;

    public static final int            KERNEL_ELEMENTS  = KERNEL_SIZE
                                                                * KERNEL_SIZE;

    public static final int            KERNEL_ELEMENTS2 = KERNEL_SIZE2
                                                                * KERNEL_SIZE2;

    public static double               ETA_LEARNIG_RATE = 0.0005;

    public static double               ERROR_THRESOLD   = 0.001;

    public static PrintWriter         out;

    static
    {
        try
        {
            out = new PrintWriter(new FileWriter("output.txt"), true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    

}
