package com.tsaky.circuitsimulator.chip.pin;

import java.awt.*;

public class PinInputOutput extends Pin{

    public PinInputOutput(String pinName, int pinID) {
        super(pinName, pinID);
    }

    public PinInputOutput(String pinName, int pinID, boolean isToggleable) {
        super(pinName, pinID, isToggleable);
    }

    public boolean isLinkHigh(){
        return getLink() != null && getLink().isLineHigh();
    }

    @Override
    public Color getPinColor() {
        return isSelected() ? Color.RED : Color.BLACK;//(isHigh() ? Color.GREEN.darker() : Color.BLACK);
    }

}
