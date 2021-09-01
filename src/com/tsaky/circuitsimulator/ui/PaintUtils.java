package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.chip.Chip;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class PaintUtils {

    public static void drawCenteredChip(Graphics g, int posX, int posY, int width, int height, Chip chip, boolean realName) {
        g.drawLine(posX - width / 2, posY - height / 2, posX - width / 5, posY - height / 2); //top line left half
        g.drawLine(posX + width / 5, posY - height / 2, posX + width / 2, posY - height / 2); //top line right half
        g.drawArc(posX - width / 5 + 1, posY - height / 2 - height / 20, (int) (.4f * width) - 2, height / 10, 180, 180); //top arc
        g.drawLine(posX - width / 2, posY + height / 2, posX + width / 2, posY + height / 2); //bottom line
        g.drawLine(posX - width / 2, posY - height / 2, posX - width / 2, posY + height / 2); //left line
        g.drawLine(posX + width / 2, posY - height / 2, posX + width / 2, posY + height / 2); // right line


        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        String chipName = realName ? chip.getSaveName() : chip.getDisplayName();

        Rectangle chipNameBounds = getStringBounds(g2d, chipName);

        float sc = (height * 0.85f) / (float) chipNameBounds.width;

        if (sc > 1.3f) sc = 1.3f;

        g2d.translate(posX, posY - height / 2f + chipNameBounds.width * sc + height / 10f);
        g2d.rotate(-Math.PI / 2);

        g2d.scale(sc, sc);
        g2d.drawString(chipName, 0, 0);

        g2d.setTransform(old);
    }

    public static int getPinSize(int pins, int height) {
        if (pins == 0) return 0;
        return height / pins;
    }

    public static Rectangle getStringBounds(Graphics g, String text) {
        Rectangle rectangle = new Rectangle();

        Font font = g.getFont();
        FontRenderContext fontRenderContext = new FontRenderContext(font.getTransform(), true, true);

        rectangle.height = font.getSize();
        rectangle.width = (int) (font.getStringBounds(text, fontRenderContext).getWidth());

        return rectangle;
    }

    public static Rectangle getMultilineStringBounds(Graphics g, String[] text) {
        int max = -1;
        for (String s : text) {
            int i = getStringBounds(g, s).width;
            if (i > max) max = i;
        }

        return new Rectangle(0, 0, max, g.getFont().getSize() * text.length);
    }

}