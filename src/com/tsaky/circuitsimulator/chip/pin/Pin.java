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

    public void paint(Graphics g, int posX, int posY, int pinSize){
        paintWithPinName(g, posX, posY, pinSize, String.valueOf(getPinID()+1));
    }

    public void paintWithPinName(Graphics g, int posX, int posY, int pinSize, String name){
        Rectangle sb = PaintUtils.getStringBounds(g, name);

        //setBounds(posX, posY, pinSize, pinSize);
        Color old = g.getColor();
        g.setColor(getPinColor());
        g.drawRect(posX, posY, getBounds().width, getBounds().height);

        g.setColor(old);
        g.drawString(name, posX + pinSize/2 - sb.width/2, posY + pinSize/2 + sb.height/2 -1); // Pin number
    }

    public Color getPinColor(){
        return isSelected() ? Color.RED : Color.BLACK;
    }

}
