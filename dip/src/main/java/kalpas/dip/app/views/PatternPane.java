package kalpas.dip.app.views;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import kalpas.dip.app.data.Image;

public class PatternPane extends Canvas
{
    private static final long serialVersionUID = 1L;

    private BufferedImage pattern       = null;

    private int           MAGNIFICATION = 6;

    private int           patterWidth   = 28;

    public void setPattern(Image image)
    {
        this.pattern = PatternPane.imageFromBytes(image.bytes, patterWidth);
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
        
        if(pattern != null)
        {
            g2.drawImage(pattern, 0, 0, pattern.getWidth() * MAGNIFICATION,
                    pattern.getHeight() * MAGNIFICATION, null);
        }
    }

    /**
     * @return the patterWidth
     */
    public int getPatterWidth()
    {
        return patterWidth;
    }

    /**
     * @param patterWidth
     *            the patterWidth to set
     */
    public void setPatterWidth(int patterWidth)
    {
        this.patterWidth = patterWidth;
    }

    /**
     * @return the mAGNIFICATION
     */
    public int getMAGNIFICATION()
    {
        return MAGNIFICATION;
    }

    /**
     * @param mAGNIFICATION the mAGNIFICATION to set
     */
    public void setMAGNIFICATION(int mAGNIFICATION)
    {
        MAGNIFICATION = mAGNIFICATION;
    }

}
