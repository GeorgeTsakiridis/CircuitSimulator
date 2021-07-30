package com.tsaky.circuitsimulator.chip.pin;

public class PinOutput extends Pin{
    boolean isOutputHigh = false;
    boolean highZMode = false;

    public PinOutput(String pinName, int pinID) {
        super(pinName, pinID);
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

    @Override
    public String toString() {
        return "PinOutput{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + '\'' +
                ", isOutputHigh=" + isOutputHigh +
                '}';
    }
}
