package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import java.awt.*;

public class Pin {

    private final String pinName;
    private final int pinID;
    private PinType pinType;
    private final Rectangle bounds;
    private boolean isSelected = false;
    private boolean isOutputHigh = false;

    public Pin(String pinName, int pinID, PinType pinType){
        this.pinName = pinName;
        this.pinID = pinID;
        setPinType(pinType);
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

    public PinType getType() {
        return pinType;
    }

    public void setPinType(PinType pinType) {
        this.pinType = pinType;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void paint(Graphics g, int offsetX, int offsetY){
        paint(g, offsetX, offsetY, String.valueOf(getPinID()+1));
    }

    //From old PinInput.java
    public boolean isLinkHigh(){
        return Linker.isLineHighForPin(this);
    }

    //From old PinGround.java
    public boolean isGrounded() {

        if(pinType == PinType.GROUND_SOURCE)return true;

        for(Pin pin : Linker.getAllConnectedPinsWith(this, null)){
            if(pin.getType() == PinType.GROUND_SOURCE || pin.getType() == PinType.OUTPUT && !pin.isHigh()){
                return true;
            }
        }

        return false;
    }

    //From old PinPower.java
    public boolean isPowered() {

        if(pinType == PinType.POWER_SOURCE)return true;

        for(Pin pin : Linker.getAllConnectedPinsWith(this, null)){
            if(pin.getType() == PinType.POWER_SOURCE || pin.getType() == PinType.OUTPUT && pin.isHigh()){
                return true;
            }
        }

        return false;
    }

    public boolean isHigh() {
        return isOutputHigh;
    }

    public void setHigh(boolean outputHigh) {
        isOutputHigh = outputHigh;
    }

    public void paint(Graphics g, int offsetX, int offsetY, String name){
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
