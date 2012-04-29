package kalpas.dip;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import kalpas.dip.general.Constants;

@SuppressWarnings("serial")
public class Visualize extends Canvas
{
    private final int            MAGNIFICATION = 3;

    private static BufferedImage input  = null;
    
    private static BufferedImage kernels = null;

    private static BufferedImage layer1 = null;

    public static void drawInput(byte[] image, int width)
    {
        Visualize.input = imageFromBytes(image, width);
    }
    
    public static void drawKernels(double[][] weights)
    {
        Visualize.kernels = imageFromDouble(weights, Constants.KERNEL_SIZE);
    }
    
    private static BufferedImage imageFromDouble(double[][] data, int width)
    {
        BufferedImage imageFromDouble = new BufferedImage(width, width
                * data.length, BufferedImage.TYPE_INT_ARGB);
        for(int feature = 0; feature < data.length; feature++)
        {
            for(int y = 0; y < width; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    int rgb = 0x0;
                    rgb |= ((int) (data[feature][y * width + x] * 127)+128);
                    rgb <<= 8;
                    rgb |= ((int) (data[feature][y * width + x] * 127)+128);
                    rgb <<= 8;
                    rgb |= ((int) (data[feature][y * width + x] * 127)+128);
                    rgb |= 0xFF000000;

                    imageFromDouble.setRGB(x, y + feature * width, invert(rgb));
                }
            }
        }

        return imageFromDouble;
    }

    public static void draw1Layer(double[] layer,int fmaps, int width)
    {
        Visualize.layer1 = imageFromDouble(layer, fmaps, width);
    }

    private static BufferedImage imageFromDouble(double[] data,int fmaps, int width)
    {
        BufferedImage imageFromDouble = new BufferedImage(width, width
                * fmaps, BufferedImage.TYPE_INT_ARGB);
        
        int featureMapElementsCount = width*width;
        for(int feature = 0; feature < fmaps; feature++)
        {
            for(int y = 0; y < width; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    int rgb = 0x0;
                    rgb |= ((int) (data[feature*featureMapElementsCount+(y * width + x)] * 127)+128);
                    rgb <<= 8;
                    rgb |= ((int) (data[feature*featureMapElementsCount+(y * width + x)] * 127)+128);
                    rgb <<= 8;
                    rgb |= ((int) (data[feature*featureMapElementsCount+(y * width + x)] * 127)+128);
                    rgb |= 0xFF000000;

                    imageFromDouble.setRGB(x, y + feature * width, invert(rgb));
                }
            }
        }

        return imageFromDouble;
    }

    private static BufferedImage imageFromBytes(byte[] bytes, int width)
    {
        BufferedImage imageFromBytes = new BufferedImage(width, bytes.length
                / width, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bytes.length / width; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int rgb = 0x0;
                rgb |= (~bytes[y * width + x] & 0xFF);
                rgb <<= 8;
                rgb |= (~bytes[y * width + x] & 0xFF);
                rgb <<= 8;
                rgb |= (~bytes[y * width + x] & 0xFF);
                rgb |= 0xFF000000;

                imageFromBytes.setRGB(x, y, rgb);
            }
        }

        return imageFromBytes;

    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        int startingPoint = 0;
        if(input != null)
        {
            g2.drawImage(input, startingPoint, 0, input.getWidth() * MAGNIFICATION,
                    input.getHeight() * MAGNIFICATION, null);
            startingPoint+=input.getWidth() * MAGNIFICATION;
        }
        if(kernels != null)
        {
            g2.drawImage(kernels, startingPoint, 0,
                    kernels.getWidth() * MAGNIFICATION, kernels.getHeight()
                            * MAGNIFICATION, null);
            startingPoint+= kernels.getWidth() * MAGNIFICATION;
        }
        if(layer1 != null)
            g2.drawImage(layer1, startingPoint, 0,
                    layer1.getWidth() * MAGNIFICATION, layer1.getHeight()
                            * MAGNIFICATION, null);
        g2.finalize();
    }

    private static int invert(int value)
    {
        return (~value) | 0xFF000000;
    }
}
