package kalpas.dip;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Visualize extends Canvas
{
    private static BufferedImage input  = null;

    private static BufferedImage layer1 = null;

    public static void drawInput(byte[] image, int width)
    {
        Visualize.input = imageFromBytes(image, width);
    }

    public static void draw1Layer(double[][] layer, int width)
    {
        Visualize.layer1 = imageFromDouble(layer, width);
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
                    rgb |= ((int) (data[feature][y * width + x] * 255));
                    rgb <<= 8;
                    rgb |= ((int) (data[feature][y * width + x] * 255));
                    rgb <<= 8;
                    rgb |= ((int) (data[feature][y * width + x] * 255));
                    rgb |= 0xFF000000;

                    imageFromDouble.setRGB(x, y + feature * width, rgb);
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
        if(input != null)
            g2.drawImage(input, 0, 0, 200, 200, null);
        if(layer1 != null)
            g2.drawImage(layer1, 200, 0, layer1.getWidth() * 3,
                    layer1.getHeight() * 3, null);
        g2.finalize();
    }
}
