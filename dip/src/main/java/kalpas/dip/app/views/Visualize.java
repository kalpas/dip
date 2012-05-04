package kalpas.dip.app.views;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import kalpas.dip.app.neural.Core;

@SuppressWarnings("serial")
public class Visualize extends Canvas
{
    private final int            MAGNIFICATION = 3;

    private static BufferedImage input         = null;

    private static BufferedImage kernels       = null;

    private static BufferedImage layer1        = null;

    private static BufferedImage layer2        = null;

    private static BufferedImage layer2weights = null;
    
    private static BufferedImage layer3 = null;

    private static BufferedImage output        = null;

    public static void drawInput(byte[] image, int width)
    {
        Visualize.input = imageFromBytes(image, width);
    }

    public static void drawKernels(double[][] weights)
    {
        Visualize.kernels = imageFromDouble(weights, Core.KERNEL_SIZE,
                Core.KERNEL_SIZE);
    }

    private static BufferedImage imageFromLayer2KernelWeights(
            double[][][] data, int width)
    {
        BufferedImage imageFromDouble = new BufferedImage(data[0].length
                * (width + 1), data.length * (width + 1),
                BufferedImage.TYPE_INT_ARGB);
        for(int feature = 0; feature < data.length; feature++)
        {
            for(int sourceMap = 0; sourceMap < data[0].length; sourceMap++)
            {
                for(int y = 0; y < width; y++)
                {
                    for(int x = 0; x < width; x++)
                    {
                        int rgb = 0x0;
                        if(y != width)
                        {
                            double value = data[feature][sourceMap][y * width
                                    + x];
                            rgb |= toRgb(value);

                        }
                        imageFromDouble.setRGB(sourceMap * (width + 1) + x,
                                feature * (width + 1) + y, rgb);
                    }
                }
            }
        }

        return imageFromDouble;
    }
    
    private static BufferedImage imageFrom3Layer(double[] data, int cols,
            int rows)
    {
        BufferedImage imageFromDouble = new BufferedImage(cols*2, rows*2 , BufferedImage.TYPE_INT_ARGB);

        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < cols; col++)
            {
                    int rgb = 0x0;
                    double value = data[row*cols+col];
                    rgb |= toRgb(value);

                    imageFromDouble.setRGB(col*2, row*2, rgb);

                }
            }

        return imageFromDouble;
    }

    private static BufferedImage imageFromDouble(double[][] data, int width,
            int height)
    {
        BufferedImage imageFromDouble = new BufferedImage(width, (height + 1)
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

    public static void draw2Kernels(double[][][] weights, int width)
    {
        Visualize.layer2weights = imageFromLayer2KernelWeights(weights, width);
    }

    public static void draw2Layer(double[] data, int fmaps, int width)
    {
           layer2 = ImageFromLayer2OutPut(data,fmaps,width);
    }
    
    public static void draw3Layer(double[] data)
    {
        layer3 = imageFrom3Layer(data, 10, 10);
    }

    public static void drawOutput(double[] data)
    {
        Visualize.output = imageFromDouble(data, 10, 1);
    }

    private static BufferedImage ImageFromLayer2OutPut(double[] data,
            int fmaps, int width)
    {
        BufferedImage imageFromDouble = new BufferedImage((width + 1) * 5,
                (width + 1) * fmaps, BufferedImage.TYPE_INT_ARGB);

        int featureMapElementsCount = width * width;
        for(int col = 0; col < 5; col++)
        {
            for(int row = 0; row < fmaps / 5; row++)
            {
                for(int y = 0; y < width + 1; y++)
                {
                    for(int x = 0; x < width; x++)
                    {
                        int rgb = 0x0;
                        if(y != width)
                        {
                            double value = data[featureMapElementsCount
                                    * (col * fmaps / 5 + row) + (y * width + x)];
                            rgb |= toRgb(value);

                        }

                        imageFromDouble.setRGB(col*(width+1)+x, y + row * (width + 1),
                                rgb);

                    }
                }
            }

        }

        return imageFromDouble;
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
            // value /= Constants.scaleY;
            int colorValue = ~(int) value & 0xFF;
            rgb |= colorValue;
            rgb <<= 8;
            rgb |= colorValue;
            rgb |= 0xFF0000;
        }
        else
        {
            value = Math.abs(value) * 255;
            // value /= Constants.scaleY;
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
        setBackground(new Color(0xFF000000));

        Graphics2D g2 = (Graphics2D) g;
        int startingPoint = 0;
        if(input != null)
        {
            g2.drawImage(input, startingPoint, 0, input.getWidth()
                    * MAGNIFICATION, input.getHeight() * MAGNIFICATION, null);
            startingPoint += input.getWidth() * MAGNIFICATION + 1;
        }
        if(kernels != null)
        {
            g2.drawImage(kernels, startingPoint, 0, kernels.getWidth()
                    * MAGNIFICATION * 3, kernels.getHeight() * MAGNIFICATION
                    * 3, null);
            startingPoint += kernels.getWidth() * MAGNIFICATION * 3 + 1;
        }
        if(layer1 != null)
        {
            g2.drawImage(layer1, startingPoint, 0, layer1.getWidth()
                    * MAGNIFICATION, layer1.getHeight() * MAGNIFICATION, null);
            startingPoint += layer1.getWidth() * MAGNIFICATION + 1;
        }
        if(layer2weights != null)
        {
            g2.drawImage(layer2weights, startingPoint, 0,
                    layer2weights.getWidth() * MAGNIFICATION,
                    layer2weights.getHeight() * MAGNIFICATION, null);
            startingPoint += layer2weights.getWidth() * MAGNIFICATION;
        }
        if(layer2 != null)
        {
            g2.drawImage(layer2, startingPoint, 0,
                    layer2.getWidth() * MAGNIFICATION,
                    layer2.getHeight() * MAGNIFICATION, null);
            startingPoint += layer2.getWidth() * MAGNIFICATION+1;
        }
        if(layer3 != null)
        {
            g2.drawImage(layer3, startingPoint, 0,
                    layer3.getWidth() * MAGNIFICATION*3,
                    layer3.getHeight() * MAGNIFICATION*3, null);
            startingPoint += layer3.getWidth() * MAGNIFICATION*3+1;
        }
        if(output != null)
        {
            g2.drawImage(output, startingPoint, 0, output.getWidth()
                    * MAGNIFICATION * 5,
                    output.getHeight() * MAGNIFICATION * 5, null);
            startingPoint += output.getWidth() * MAGNIFICATION * 5;
        }

        g2.finalize();
    }
}
