package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class ViewportPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final Color backgroundColor = new Color(220, 220, 220);
    private final Color gridColor = new Color(100, 100, 100, 50);

    private int offsetX = 0;
    private int offsetY = 0;
    private float scale = 1f;

    private ArrayList<Chip> chips = null;
    private Chip ghostChip = null;

    private ViewMode viewMode = ViewMode.NORMAL;
    private final Handler handler;

    public ViewportPanel(Handler handler) {
        this.handler = handler;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setViewMode(ViewMode viewMode){
        this.viewMode = viewMode;
    }

    public void addOffset(int offsetX, int offsetY){
        this.offsetX += offsetX/scale;
        this.offsetY += offsetY/scale;
    }

    public void resetOffsetAndScale(){
        offsetX = offsetY = 0;
        scale = 1f;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public float getScale(){ return scale; }

    public void setScale(float scale){ this.scale = scale; }

    public void setChipsToPaint(ArrayList<Chip> chips){
        this.chips = chips;
    }

    public void updateGhostChip(Chip chip){
        ghostChip = chip;
    }

    public void increaseScale(){
        scale += 0.1f;
    }

    public void decreaseScale(){
        scale -= 0.1f;
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(gridColor);

        for(int i = -20; i < getWidth() + 20; i += 20*scale){
            g.drawLine(i + offsetX%20, 0, i + offsetX%20, getHeight()-1);
        }

        for (int i = -20; i < getHeight() + 20; i+= 20*scale) {
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

        Linker.paint(g, viewMode, offsetX, offsetY);

        if(Handler.SHORTED){
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            g.drawString("Short Circuit!", 10, 20);
            g.drawString("Short Circuit!", 10, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-70, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-70, 20);
            Handler.SHORTED = false;
        }

        g.setColor(Color.BLACK);
    }

    private int getMouseX(){
        Point mousePos = getMousePosition();
        if(mousePos == null)return -1;

        return (int)((mousePos.x)/getScale()) - getOffsetX();
    }

    private int getMouseY(){
        Point mousePos = getMousePosition();
        if(mousePos == null)return -1;

        return (int)((mousePos.y)/getScale()) - getOffsetY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = getMouseX();
        int y = getMouseY();
        if(x != -1 && y != -1) handler.mouseClicked(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = getMousePosition();
        handler.mousePressed(p.x, p.y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = getMouseX();
        int y = getMouseY();
        if(x != -1 && y != -1) handler.mouseReleased(x, y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = getMouseX();
        int y = getMouseY();
        Point p = getMousePosition();
        if(x != -1 && y != -1) handler.mouseDragged(x, y, p.x, p.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = getMouseX();
        int y = getMouseY();

        if(x != -1 && y != -1) handler.mouseMoved(x, y);
    }
}
