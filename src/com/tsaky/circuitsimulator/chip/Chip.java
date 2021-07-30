package com.tsaky.circuitsimulator.chip;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.ui.PaintUtils;
import com.tsaky.circuitsimulator.chip.pin.*;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a component, not necessarily an actual chip. E.g. a switch, an LED etc.
 */
public abstract class Chip {

    private final String chipName; //The name of the chip
    private final Pin[] pins; //A Pin array that contains all pins of the chip
    private int posX = 0; //The X position of the chip on the viewport
    private int posY = 0; //The Y position of the chip on the viewport
    private int width = 0; //The width of the chip not containing the pins
    private int height = 0; //The height of the chip not containing the pins
    private final Rectangle border; //The border of the chip containing the pins
    private boolean isSelected = false; //Whether the chip is selected
    private int pinSize; //The width/height of each pin rectangle
    private InfoPage infoPage; //The info page of the chip

    /**
     * Constructor: Creates a Chip instance.
     * @param chipName The name of the chip
     * @param infoPage The InfoPage of the chip
     * @param pins The pins of the chip
     */
    public Chip(String chipName, InfoPage infoPage, Pin[] pins) {
        this.chipName = chipName;
        this.infoPage = infoPage;
        this.pins = pins;
        border = new Rectangle();
    }

    /**
     *  Calculates the outputs of the chip's pins.
     */
    public abstract void calculateOutputs();

    /**
     *  Sets a new InfoPage for the chip. (Not used)
     * @param infoPage The new InfoPage
     */
    public void setInfoPage(InfoPage infoPage){
        this.infoPage = infoPage;
    }

    /**
     * Returns the InfoPage of the chip
     * @return The InfoPage of the chip
     */
    public InfoPage getInfoPage(){
        return infoPage;
    }

    /**
     * Returns true or false depending on whether or not the chip is powered.
     * @return True if the chip is powered, false otherwise
     */
    public boolean isPowered(){
        PinPower power = getPowerPin();
        PinGround ground = getGroundPin();
        if(power == null || ground == null)return true;
        return power.isPowered() && ground.isGrounded();
    }

    /**
     * Turns off all outputs pins of the chip.
     */
    public void turnAllOutputsOff(){
        for(Pin pin : pins){
            if(pin instanceof PinOutput){
                ((PinOutput)pin).setHigh(false);
            }
        }
    }

    /**
     * Turns all outputs pins of the chip to High Impedance mode.
     */
    public void turnAllOutputsToHighZ(){
        for(Pin pin : pins){
            if(pin instanceof PinOutput){
                ((PinOutput) pin).setHighZMode(true);
            }
        }
    }

    /**
     * Returns the pin in index {@code pin} of the Pins array if it is a PinInput or PinInputOutput.
     * @param pin The index of the pin
     * @return A PinInput if the pin at index {@code pin} is an PinInput or PinInputOutput, null otherwise
     */
    public PinInput getInputPin(int pin){
        if(pins[pin] instanceof PinInput){
            return (PinInput) pins[pin];
        }
        return null;
    }

    /**
     * Returns the pin in index {@code pin} of the Pins array if it is a PinOutput or PinInputOutput.
     * @param pin The index of the pin
     * @return A PinInput if the pin at index {@code pin} is an PinOutput or PinInputOutput, null otherwise
     */
    public PinOutput getOutputPin(int pin){
        if(pins[pin] instanceof PinOutput){
            return (PinOutput) pins[pin];
        }
        return null;
    }

    /**
     * Returns the ground pin of the chip if it has one
     * @return The PinGround of the chip if the has one, null otherwise
     */
    public PinGround getGroundPin(){
        for(Pin pin : pins){
            if(pin instanceof PinGround)return (PinGround)pin;
        }

        return null;
    }

    /**
     * Returns the power pin of the chip if it has one
     * @return The PinPower of the chip if the has one, null otherwise
     */
    public PinPower getPowerPin(){
        for(Pin pin : pins){
            if(pin instanceof PinPower)return (PinPower)pin;
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
     * @param isSelected
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
     * Returns the border of the chip.
     * @return a Rectangle representing the borders of the chip
     */
    public Rectangle getBorder(){
        return border;
    }

    /**
     * Sets a new X-Y Position for the chip and updates it's borders
     * @param posX The new X Coordinate of the chip
     * @param posY The new Y Coordinate of the chip
     */
    public void setPosition(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        border.setLocation(posX - (width + 3*pinSize)/2, posY - height/2);
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
     * Changes the size of the chip and updates it's borders without taking into account the pins.
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
     * @return the Chip name
     */
    public String getChipName(){
        return chipName;
    }

    /**
     * @param pin index of the pin
     * @return the name of the pin
     */
    public String getPinName(int pin){
        return pins[pin].getName();
    }

    /**
     * @return the bytes of the extra data; this data can be used for example store a state of a switch
     */
    public byte[] getExtraDataBytes(){
        return null;
    }

    /**
     * If extra data bytes were given when saving the chip, this function will return these bytes when loadings
     * @param bytes
     */
    public void setExtraData(byte[] bytes){}

    /**
     * Creates a new instance of the chip
     * @return A new Chip instance
     */
    public Chip createNewInstance() {
        try {
            return this.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Draws the Chip
     * @param g
     * @param offsetX
     * @param offsetY
     */
    public void paintComponent(Graphics g, int offsetX, int offsetY){
        if(getPinNumber() == 0)return;

        PaintUtils.drawCenteredChip(g, getPosX() + offsetX, getPosY() + offsetY, getWidth(), getHeight(), this);
        pinSize = PaintUtils.getPinSize(getPinNumber(), height);
        int sidePins = getPinNumber()/2;
        int spacePerPin = height/sidePins - pinSize;

        for (int i = 0; i < getPinNumber()/2; i++) {
            int pinY = posY - height/2 + (spacePerPin + pinSize)*i + pinSize/2;
            int pinX1 = posX - width/2 - pinSize;
            int pinX2 = posX + width/2;

            getPin(i).setBounds(pinX1, pinY, pinSize, pinSize);
            getPin(getPinNumber()-i-1).setBounds(pinX2, pinY, pinSize, pinSize);

            getPin(i).paint(g, offsetX, offsetY);
            getPin(getPinNumber()- i - 1).paint(g, offsetX, offsetY);
        }
    }

    @Override
    public String toString() {
        return "Chip{" +
                "chipName='" + chipName + '\'' +
                ", pins=" + Arrays.toString(pins) +
                ", posX=" + posX +
                ", posY=" + posY +
                ", width=" + width +
                ", height=" + height +
                ", border=" + border +
                ", isSelected=" + isSelected +
                '}';
    }
}
