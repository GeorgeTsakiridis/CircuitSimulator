package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.ui.PaintUtils;

import java.awt.*;

public abstract class Pin {

    private final String pinName;
    private final int pinID;
    private Rectangle bounds;
    private boolean isSelected = false;

    public Pin(String pinName, int pinID){
        this.pinName = pinName;

        this.pinID = pinID;
        this.bounds = new Rectangle();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void setBounds(int x, int y, int width, int height){
        bounds.setBounds(x, y, width, height);
    }

    public String getName(){
        return pinName;
    }

    public int getPinID() {
        return pinID;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void paint(Graphics g, int offsetX, int offsetY){
        paintWithPinName(g, offsetX, offsetY, String.valueOf(getPinID()+1));
    }

    public void paintWithPinName(Graphics g, int offsetX, int offsetY, String name){
        Rectangle sb = PaintUtils.getStringBounds(g, name);

        //setBounds(posX, posY, pinSize, pinSize);
        Color old = g.getColor();
        g.setColor(getPinColor());
        g.drawRect(bounds.x + offsetX, bounds.y + offsetY, getBounds().width, getBounds().height);

        g.setColor(old);
        g.drawString(name, bounds.x + bounds.width/2 - sb.width/2 + offsetX,
                bounds.y + bounds.height/2 + sb.height/2 -1 + offsetY);
    }

    public Color getPinColor(){
        return isSelected() ? Color.RED : Color.BLACK;
    }

}
