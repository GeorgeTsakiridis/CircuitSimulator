package com.tsaky.circuitsimulator.ui.window;

import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipUtils;
import com.tsaky.circuitsimulator.ui.LineViewMode;
import com.tsaky.circuitsimulator.ui.PinViewMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class ViewportPanel extends JPanel implements MouseListener, MouseMotionListener {

    public static boolean PAINTING = false;

    private final Color backgroundColor = new Color(220, 220, 220);
    private final Color gridColor = new Color(100, 100, 100, 50);

    private int offsetX = 0;
    private int offsetY = 0;
    private float scale = 1f;
    private boolean renderChipRealName = false;
    private boolean mouseSnapEnabled = false;
    private boolean paintGrid = true;

    private ArrayList<Chip> chips = null;
    private Chip ghostChip = null;

    public static LineViewMode lineViewMode = LineViewMode.NORMAL;
    public static PinViewMode pinViewMode = PinViewMode.NORMAL;

    private final Handler handler;

    public ViewportPanel(Handler handler) {
        this.handler = handler;
        addMouseListener(this);
        addMouseMotionListener(this);
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
        if(ghostChip != null)ghostChip.onRemove();
        ghostChip = chip;
    }

    public void increaseScale(){
        if(scale < 1.0f)scale += 0.1f;
    }

    public void decreaseScale(){
        if(scale > 0.2f)scale -= 0.1f;
    }

    public void setRenderChipRealName(boolean renderChipRealName){
        this.renderChipRealName = renderChipRealName;
    }

    public void setMouseSnapEnabled(boolean mouseSnapEnabled){
        this.mouseSnapEnabled = mouseSnapEnabled;
    }

    public boolean isMouseSnapEnabled() {
        return mouseSnapEnabled;
    }

    public void toggleGrid() {
        paintGrid = !paintGrid;
    }

    @Override
    public void paintComponent(Graphics g) {
        PAINTING = true;

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        if(paintGrid) {
            g.setColor(gridColor);

            for (int i = 0; i < getWidth() + 20; i += 20 * scale) {
                g.drawLine(i + offsetX % 20, 0, i + offsetX % 20, getHeight() - 1);
            }

            for (int i = 0; i < getHeight() + 20; i += 20 * scale) {
                g.drawLine(0, i + offsetY % 20, getWidth() - 1, i + offsetY % 20);
            }
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.scale(scale, scale);

        if(chips != null) {
            for (Chip chip : chips) {
                g.setColor(chip.isSelected() ? Color.RED : Color.BLACK);
                chip.paintComponent(g, offsetX, offsetY, renderChipRealName, false);
            }
        }

        if(ghostChip != null){
            g.setColor(ChipUtils.chipCollidesWithOtherChip(ghostChip, chips) ? Color.RED : Color.GREEN.darker());
            if(isMouseSnapEnabled()){
                ghostChip.roundPosition();
            }

            ghostChip.paintComponent(g, offsetX, offsetY, renderChipRealName, false);
        }

        Linker.paint(g, offsetX, offsetY);

        if(ViewportPanel.pinViewMode == PinViewMode.TYPE){

            g2d.scale(1d/scale, 1d/scale);
            Color oldColor = g2d.getColor();
            int x = getWidth()-180;
            int y = getHeight() - g2d.getFont().getSize()*6;

            drawLegendLine(g, Color.RED, "Power Source/Input", x, y);
            y += g2d.getFont().getSize();
            drawLegendLine(g, Color.BLUE, "Ground Source/Input", x, y);
            y += g2d.getFont().getSize();
            drawLegendLine(g, Color.GREEN, "Input", x, y);
            y += g2d.getFont().getSize();
            drawLegendLine(g, Color.ORANGE, "Output", x, y);
            y += g2d.getFont().getSize();
            drawLegendLine(g, Color.MAGENTA, "High-Z", x, y);
            y += g2d.getFont().getSize();
            drawLegendLine(g, Color.WHITE, "Not Used", x, y);

            g2d.setColor(oldColor);
            g2d.scale(scale*2d, scale*2d);
        }

        if(Handler.SHORTED){
            g2d.scale(1d/scale, 1d/scale);
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            g.drawString("Short Circuit!", 10, 20);
            g.drawString("Short Circuit!", 10, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-100, getHeight()-13);
            g.drawString("Short Circuit!", getWidth()-100, 20);
            g2d.scale(scale, scale);
            Handler.SHORTED = false;
        }

        g.setColor(Color.BLACK);

        PAINTING = false;
    }

    private void drawLegendLine(Graphics g, Color color, String text, int x, int y){
        g.setColor(color);
        if(color == Color.WHITE){
            g.setColor(Color.BLACK);
            g.drawRect(x, y-5, 10, 10);
        }else {
            g.fillRect(x, y - 5, 10, 10);
        }
        g.setColor(Color.BLACK);
        g.drawString(text, x+20, y+5);
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

        if (x != -1 && y != -1) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                handler.mouseClicked(x, y, true);
            }
            else{
                handler.mouseClicked(x, y, false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = getMouseX();
        int y = getMouseY();

        Point p = getMousePosition();
        if(x != -1 && y != -1) handler.mousePressed(x, y, p.x, p.y);
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
