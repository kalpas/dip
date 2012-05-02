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

    private static BufferedImage input         = null;

    private static BufferedImage kernels       = null;

    private static BufferedImage layer1        = null;

    private static BufferedImage layer2weights = null;

    private static BufferedImage output        = null;

    public static void drawInput(byte[] image, int width)
    {
        Visualize.input = imageFromBytes(image, width);
    }

    public static void drawKernels(double[][] weights)
    {
        Visualize.kernels = imageFromDouble(weights, Constants.KERNEL_SIZE,Constants.KERNEL_SIZE);
    }

    private static BufferedImage imageFromDouble(double[][] data, int width, int height)
    {
        BufferedImage imageFromDouble = new BufferedImage(width, (height+1)
                * data.length, BufferedImage.TYPE_INT_ARGB);
        for(int feature = 0; feature < data.length; feature++)
        {
            for(int y = 0; y < height + 1; y++)
            {
                for(int x = 0; x < width; x++)
                {

                    int rgb = 0x0;
                    if(y != height)
                    {
                        double value = data[feature][(y * width + x)];
                        rgb |= toRgb(value);

                    }
                    imageFromDouble.setRGB(x, y + feature * (height + 1), rgb);
                }
            }
        }

        return imageFromDouble;
    }

    public static void draw1Layer(double[] layer, int fmaps, int width)
    {
        Visualize.layer1 = imageFromDouble(layer, fmaps, width);
    }
    
    public static void draw2LayerWeights(double[][] weights, int width, int height)
    {
        Visualize.layer2weights = imageFromDouble(weights, width, height);
    }
    
    public static void drawOutput(double[] data)
    {
        Visualize.output = imageFromDouble(data, 10, 1);
    }
    
    private static BufferedImage imageFromDouble(double[] data, int fmaps,
            int width)
    {
        BufferedImage imageFromDouble = new BufferedImage(width, (width + 1)
                * fmaps, BufferedImage.TYPE_INT_ARGB);

        int featureMapElementsCount = width * width;
        for(int feature = 0; feature < fmaps; feature++)
        {
            for(int y = 0; y < width + 1; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    int rgb = 0x0;
                    if(y != width)
                    {
                        double value = data[feature * featureMapElementsCount
                                + (y * width + x)];
                        rgb |= toRgb(value);

                    }

                    imageFromDouble.setRGB(x, y + feature * (width + 1), rgb);

                }
            }

        }

        return imageFromDouble;
    }

    public static int toRgb(double value)
    {
        int rgb = 0x0;
        if(value > 0)
        {
            value *= 255;
//            value /= Constants.scaleY;
            int colorValue = ~(int) value & 0xFF;
            rgb |= colorValue;
            rgb <<= 8;
            rgb |= colorValue;
            rgb |= 0xFF0000;
        }
        else
        {
            value = Math.abs(value) * 255;
//            value /= Constants.scaleY;
            int colorValue = ~(int) value & 0xFF;
            rgb |= colorValue;
            rgb <<= 8;
            rgb |= colorValue;
            rgb <<= 8;
            rgb |= 0xFF;

        }
        rgb |= 0xFF000000;
        return rgb;

    }

    private static BufferedImage imageFromBytes(byte[] bytes, int width)
    {
        BufferedImage imageFromBytes = new BufferedImage(width, bytes.length
                / width, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bytes.length / width; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int value = ~bytes[y * width + x] & 0xFF;
                int rgb = 0x0;
                rgb |= value;
                rgb <<= 8;
                rgb |= value;
                rgb <<= 8;
                rgb |= value;
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
            g2.drawImage(input, startingPoint, 0, input.getWidth()
                    * MAGNIFICATION, input.getHeight() * MAGNIFICATION, null);
            startingPoint += input.getWidth() * MAGNIFICATION;
        }
        if(kernels != null)
        {
            g2.drawImage(kernels, startingPoint, 0, kernels.getWidth()
                    * MAGNIFICATION * 3, kernels.getHeight() * MAGNIFICATION
                    * 3, null);
            startingPoint += kernels.getWidth() * MAGNIFICATION * 3;
        }
        if(layer1 != null)
        {
            g2.drawImage(layer1, startingPoint, 0, layer1.getWidth()
                    * MAGNIFICATION, layer1.getHeight() * MAGNIFICATION, null);
            startingPoint += layer1.getWidth()
                    * MAGNIFICATION;
        }
        if(layer2weights!=null)
        {
            g2.drawImage(layer2weights, startingPoint, 0, layer2weights.getWidth()
                    * MAGNIFICATION, layer2weights.getHeight() * MAGNIFICATION, null);
            startingPoint += layer2weights.getWidth()
                    * MAGNIFICATION;
        }
        if(output !=null)
        {
            g2.drawImage(output, startingPoint, 0, output.getWidth()
                    * MAGNIFICATION * 3, output.getHeight() * MAGNIFICATION * 3, null);
            startingPoint += output.getWidth()
                    * MAGNIFICATION * 3;
        }
            
        g2.finalize();
    }
}
