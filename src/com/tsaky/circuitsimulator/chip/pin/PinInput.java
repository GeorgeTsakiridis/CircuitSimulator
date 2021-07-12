package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.Linker;

import java.awt.*;

public class PinInput extends Pin{

    boolean isOutputHigh = false;
    boolean highZMode = false;
    boolean inputMode = false;

    public PinInput(String pinName, int pinID) {
        super(pinName, pinID);
    }

    public boolean isLinkHigh(){
        return Linker.isLineHighForPin(this);
    }

    @Override
    public Color getPinColor() {

        if(isSelected())return Color.RED;
        if(!inputMode){
            if(isLinkHigh())return Color.GREEN;
        }
        return Color.BLACK;
    }

    public boolean isHigh() {
        return isOutputHigh;
    }

    public void setHigh(boolean outputHigh) {
        highZMode = false;
        isOutputHigh = outputHigh;
    }

    public boolean isHighZMode() {
        return highZMode;
    }

    public void setHighZMode(boolean highZMode) {
        this.highZMode = highZMode;
    }

    public boolean isInInputMode() {
        return inputMode;
    }

    public void setInputMode(boolean inputMode) {
        this.inputMode = inputMode;
    }
}
