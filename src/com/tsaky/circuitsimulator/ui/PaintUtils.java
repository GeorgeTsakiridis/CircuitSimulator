package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.chip.Chip;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class PaintUtils {

    public static void drawCenteredChip(Graphics g, int posX, int posY, int width, int height, Chip chip){
        g.drawLine(posX-width/2, posY-height/2, posX-width/5, posY-height/2); //top line left half
        g.drawLine(posX+width/5, posY-height/2, posX+width/2, posY-height/2); //top line right half
        g.drawArc(posX-width/5+1, posY-height/2-height/20, (int)(.4f*width)-2, height/10, 180, 180); //top arc
        g.drawLine(posX-width/2, posY+height/2, posX+width/2, posY+height/2); //bottom line
        g.drawLine(posX-width/2, posY-height/2, posX-width/2, posY+height/2); //left line
        g.drawLine(posX+width/2, posY-height/2, posX+width/2, posY+height/2); // right line


        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

        Rectangle chipNameBounds = getStringBounds(g2d, chip.getDisplayName());

        float sc = (height*0.85f)/(float)chipNameBounds.width;

        if(sc > 1.3f)sc = 1.3f;

        g2d.translate(posX, posY-height/2f + chipNameBounds.width*sc + height/10f);
        g2d.rotate(-Math.PI/2);

        g2d.scale(sc, sc);
        g2d.drawString(chip.getDisplayName(), 0, 0);

        g2d.setTransform(old);
    }

    public static int getPinSize(int pins, int height){
        if(pins == 0)return 0;
        return height/pins;
    }

    public static Rectangle getStringBounds(Graphics g, String text){
        Rectangle rectangle = new Rectangle();

        Font font = g.getFont();
        FontRenderContext fontRenderContext = new FontRenderContext(font.getTransform(), true, true);

        rectangle.height = font.getSize();
        rectangle.width = (int)(font.getStringBounds(text, fontRenderContext).getWidth());

        return rectangle;
    }

    public static Rectangle getMultilineStringBounds(Graphics g, String[] text){
        int max = -1;
        for(String s : text){
            int i = getStringBounds(g, s).width;
            if(i > max)max = i;
        }

        return new Rectangle(0, 0, max, g.getFont().getSize() * text.length);
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();
        if(width <= 0 || height <= 0)return null;

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
