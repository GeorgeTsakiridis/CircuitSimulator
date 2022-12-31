package eu.gtsak.circuitsimulator.chip;

import eu.gtsak.circuitsimulator.ui.PaintUtils;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a component, not necessarily an actual chip. E.g. a switch, an LED etc.
 */
public abstract class Chip {

    private final String chipSaveName; //The save name of the chip
    private String chipDisplayName; //The display name of the chip
    private final Pin[] pins; //A Pin array that contains all pins of the chip
    private int posX = 0; //The X position of the chip on the viewport
    private int posY = 0; //The Y position of the chip on the viewport
    private int width = 0; //The width of the chip not containing the pins
    private int height = 0; //The height of the chip not containing the pins
    private final Rectangle border; //The border of the chip containing the pins
    private boolean isSelected = false; //Whether the chip is selected
    private int pinSize; //The width/height of each pin rectangle

    /**
     * Constructor: Creates a Chip instance.
     * @param chipSaveName The save name of the chip
     * @param chipDisplayName The display name of the chip
     * @param pins The pins of the chip
     */
    public Chip(String chipSaveName, String chipDisplayName, Pin[] pins) {
        this.chipSaveName = chipSaveName;
        this.chipDisplayName = chipDisplayName;
        this.pins = pins;
        border = new Rectangle();
    }

    /**
     *  Calculates the outputs of the chip's pins.
     */
    public abstract void calculate();

    /**
     * Returns the Description of the Chip.
     */
    public abstract String getDescription();

    /**
     * Returns true or false depending on whether the chip is powered.
     * @return True if the chip is powered, false otherwise
     */
    public boolean isPowered(){
        Pin powerPin = getPowerPin();
        Pin groundPin = getGroundPin();
        if(powerPin == null || groundPin == null)return true;
        return powerPin.isPowered() && groundPin.isGrounded();
    }

    /**
     * Turns off all outputs pins of the chip.
     */
    public void turnAllOutputsOff(){
        for(Pin pin : pins){
            if(pin.getType() == PinType.OUTPUT){
                pin.setHigh(false);
            }
        }
    }

    /**
     * Turns all pins to another PinType.
     */
    public void turnAllPinTypesTo(Pin[] pins, PinType to){
        for(Pin pin : pins){
            pin.setType(to);
        }
    }

    /**
     * Turns all pins of one PinType to another PinType.
     */
    public void turnAllPinTypesTo(PinType from, PinType to){
        for(Pin pin : pins){
            if(pin.getType() == from)pin.setType(to);
        }
    }

    /**
     * Returns the ground pin of the chip if it has one
     * @return The PinGround of the chip if the has one, null otherwise
     */
    public Pin getGroundPin(){
        for(Pin pin : pins){
            if(pin.getType() == PinType.GROUND)return pin;
        }

        return null;
    }

    /**
     * Returns the power pin of the chip if it has one
     * @return The PinPower of the chip if the has one, null otherwise
     */
    public Pin getPowerPin(){
        for(Pin pin : pins){
            if(pin.getType() == PinType.POWER)return pin;
        }

        return null;
    }

    /**
     * Returns the pin array of the chip
     * @return the pin array of the chip
     */
    public Pin[] getPins(){
        return pins;
    }

    /**
     * Returns the pin in index {@code pin} of the Pins array
     * @param pin The index of the pin
     * @return the Pin at index {@code pin} of the Pins array
     */
    public Pin getPin(int pin){
        return pins[pin];
    }

    /**
     * Returns true of false depending on whether the chip is selected
     * @return true if the chip is selected, false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets the isSelected boolean of the Chip to true
     */
    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    /**
     * Executes when the user toggles the chip
     */
    public void toggle(){

    }

    /**
     * Executes when the component is created via the Add tool.
     */
    public void onAdded(){

    }

    /**
     * Executes when the component is removed
     */
    public void onRemove(){

    }

    /**
     * Returns the border of the chip.
     * @return a Rectangle representing the borders of the chip
     */
    public Rectangle getBorder(){
        return border;
    }

    /**
     * Sets a new X-Y Position for the chip and updates its borders
     * @param posX The new X Coordinate of the chip
     * @param posY The new Y Coordinate of the chip
     */
    public void setPosition(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        border.setLocation(posX - (width + 3*pinSize)/2, posY - height/2);
    }

    /**
     * Rounds the posX and posY of the chip
     */
    public void roundPosition(){

        if(getPosX() >= 0){
            posX = ((getPosX()+10)/20)*20;
        }else{
            posX = ((getPosX()-10)/20)*20;
        }

        if(getPosY() >= 0){
            posY = ((getPosY()+10)/20)*20;
        }else{
            posY = ((getPosY()-10)/20)*20;
        }

    }

    /**
     * Changes the size of the chip and updates its borders taking into account the pins.
     * {@code width} and {@code height} should not include the pins
     * @param width The new width of the chip
     * @param height The new height of the chip
     */
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        pinSize = PaintUtils.getPinSize(getPinNumber(), getHeight());
        border.setSize(width + 3*pinSize, height + 4);
    }

    /**
     * Changes the size of the chip and updates its borders without taking into account the pins.
     * @param width The new width of the chip
     * @param height The new height of the chip
     */
    public void setSizeWithoutPins(int width, int height){
        this.width = width;
        this.height = height;
        border.setSize(width, height);
    }

    /**
     * @return the width of the chip
     */
    public int getWidth(){
        return width;
    }

    /**
     * @return the height of the chip
     */
    public int getHeight(){
        return height;
    }

    /**
     * @return the X coordinate of the chip
     */
    public int getPosX(){
        return posX;
    }

    /**
     * @return the Y coordinate of the chip
     */
    public int getPosY(){
        return posY;
    }

    /**
     * @return the total pins of the chip
     */
    public int getPinNumber(){
        if(pins == null)return 0;
        return pins.length;
    }

    /**
     * Changes the display name of the chip
     */
    public void setDisplayName(String chipDisplayName){
        this.chipDisplayName = chipDisplayName;
    }

    /**
     * @return the Chip save name
     */
    public String getSaveName(){
        return chipSaveName;
    }

    /**
     * @return the Chip display name
     */
    public String getDisplayName(){
        return chipDisplayName;
    }

    /**
     * @return the bytes of the extra data; this data can be used for example store a state of a switch
     */
    public byte[] getExtraDataBytes(){
        return null;
    }

    /**
     * If extra data bytes were given when saving the chip, this function will return these bytes when loadings
     */
    public void setExtraData(byte[] bytes){}

    /**
     * Creates a new instance of the chip
     * @return A new Chip instance
     */
    public Chip createNewInstance() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Draws the Chip
     */
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription){
        if(getPinNumber() == 0)return;

        PaintUtils.drawCenteredChip(g, getPosX() + offsetX, getPosY() + offsetY, getWidth(), getHeight(), this, realName);
        pinSize = PaintUtils.getPinSize(getPinNumber(), height);
        int sidePins = getPinNumber()/2;
        int spacePerPin = height/sidePins - pinSize;

        for (int i = 0; i < getPinNumber()/2; i++) {
            int pinY = getPosY() - height/2 + (spacePerPin + pinSize)*i + pinSize/2;
            int pinX1 = getPosX() - width/2 - pinSize;
            int pinX2 = getPosX() + width/2;

            getPin(i).setBounds(pinX1, pinY, pinSize, pinSize);
            getPin(getPinNumber()-i-1).setBounds(pinX2, pinY, pinSize, pinSize);

            getPin(i).paint(g, offsetX, offsetY, pinDescription, true);
            getPin(getPinNumber()- i - 1).paint(g, offsetX, offsetY, pinDescription, false);
        }
    }

    @Override
    public String toString() {
        return "Chip{" +
                "chipSaveName='" + chipSaveName + '\'' +
                ", chipDisplayName='" + chipDisplayName + '\'' +
                ", pins=" + Arrays.toString(pins) +
                ", posX=" + getPosX() +
                ", posY=" + getPosY() +
                ", width=" + width +
                ", height=" + height +
                ", border=" + border +
                ", isSelected=" + isSelected +
                '}';
    }
}
