package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.Linker;

import java.awt.*;

public class PinInput extends Pin{

    boolean isOutputHigh = false;

    public PinInput(String pinName, int pinID) {
        super(pinName, pinID);
    }

    public boolean isLinkHigh(){
        return Linker.isLineHighForPin(this);
    }

    public void setHigh(boolean outputHigh) {
        isOutputHigh = outputHigh;
    }

}
