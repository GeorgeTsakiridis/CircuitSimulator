package eu.gtsak.circuitsimulator.chip.pin;

import eu.gtsak.circuitsimulator.logic.Linker;
import eu.gtsak.circuitsimulator.ui.PaintUtils;
import eu.gtsak.circuitsimulator.ui.PinViewMode;
import eu.gtsak.circuitsimulator.ui.window.ViewportPanel;

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
        setType(pinType);
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

    public void setType(PinType pinType) {
        this.pinType = pinType;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void paint(Graphics g, int offsetX, int offsetY, String pinName){
        paint(g, offsetX, offsetY, pinName, false, true);
    }

    public void paint(Graphics g, int offsetX, int offsetY, boolean description, boolean leftSide){
        paint(g, offsetX, offsetY, String.valueOf(getPinID()+1), description, leftSide);
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

    public void paint(Graphics g, int offsetX, int offsetY, String name, boolean description, boolean leftSide){
        Rectangle inNameBounds = PaintUtils.getStringBounds(g, name);
        Rectangle outNameBounds = PaintUtils.getStringBounds(g, getName());

        //setBounds(posX, posY, pinSize, pinSize);
        Color old = g.getColor();
        g.setColor(getColor());
        g.drawRect(bounds.x + offsetX, bounds.y + offsetY, getBounds().width, getBounds().height);

        switch (ViewportPanel.pinViewMode) {
            case STATUS -> g.setColor(getStatusColor());
            case TYPE -> g.setColor(getTypeColor());
        }

        if(ViewportPanel.pinViewMode != PinViewMode.NORMAL && g.getColor() != Color.WHITE){
            g.fillRect(bounds.x + offsetX + 1, bounds.y + offsetY + 1, getBounds().width-1, getBounds().height-1);
        }

        g.setColor(old);
        g.drawString(name, bounds.x + bounds.width/2 - inNameBounds.width/2 + offsetX,
                bounds.y + bounds.height/2 + inNameBounds.height/2 -1 + offsetY);

        if(description){
            int x = leftSide ? bounds.x - outNameBounds.width + offsetX - 10 :
                    bounds.x + bounds.width + offsetX + 10;
            g.drawString(getName(), x,
                    bounds.y + bounds.height/2 + outNameBounds.height/2 -1 + offsetY);
        }

    }

    public Color getColor(){
        return isSelected() ? Color.RED : Color.BLACK;
    }

    public Color getStatusColor(){
        if(getType() == PinType.OUTPUT){
            return isHigh() ? Color.GREEN : Color.RED;
        } else{
            return Color.WHITE;
        }
    }

    public Color getTypeColor(){
        return switch (pinType) {
            case INPUT -> Color.GREEN;
            case OUTPUT -> Color.ORANGE;
            case HIGH_Z -> Color.MAGENTA;
            case GROUND, GROUND_SOURCE -> Color.BLUE;
            case POWER, POWER_SOURCE -> Color.RED;
            case NOT_USED -> Color.WHITE;
        };
    }

}
