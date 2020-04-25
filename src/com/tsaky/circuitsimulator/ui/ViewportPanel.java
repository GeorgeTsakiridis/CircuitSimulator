package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewportPanel extends JPanel {

    private final Color backgroundColor = new Color(220, 220, 220);
    private final Color gridColor = new Color(100, 100, 100, 50);

    private int offsetX = 0;
    private int offsetY = 0;
    private float scale = 1f;

    private Linker linker;
    private ArrayList<Chip> chips = null;
    private Chip ghostChip = null;

    public ViewportPanel(Linker linker){
        this.linker = linker;
    }

    public void addOffset(int offsetX, int offsetY){
        this.offsetX += (int)((float)offsetX/scale);
        this.offsetY += (int)((float)offsetY/scale);
    }

    public void resetOffset(){
        offsetX = offsetY = 0;
        scale = 1f;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setChipsToPaint(ArrayList<Chip> chips){
        this.chips = chips;
    }

    public void updateGhostChip(Chip chip){
        ghostChip = chip;
    }

    public void increaseScale(){
        scale += 0.2f;
    }

    public void decreaseScale(){
        scale -= 0.2f;
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(gridColor);

        for(int i = -20; i < getWidth() + 20; i += 20){
            g.drawLine(i + offsetX%20, 0, i + offsetX%20, getHeight()-1);
        }

        for (int i = -20; i < getHeight() + 20; i+= 20) {
            g.drawLine(0, i + offsetY%20, getWidth()-1, i + offsetY%20);
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.scale(scale, scale);

        if(chips != null) {
            for (Chip chip : chips) {
                g.setColor(chip.isSelected() ? Color.RED : Color.BLACK);
                chip.paintComponent(g, offsetX, offsetY);
            }
        }

        if(ghostChip != null){
            g.setColor(ChipUtils.chipCollidesWithOtherChip(ghostChip, chips) ? Color.RED : Color.GREEN.darker());
            ghostChip.paintComponent(g, offsetX, offsetY);
        }

        linker.paint(g);

        if(Handler.SHORTED){
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            g.drawString("Short Circuit!", 10, 20);
            g.drawString("Short Circuit!", 10, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-70, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-70, 20);
            Handler.SHORTED = false;
        }

    }

}
