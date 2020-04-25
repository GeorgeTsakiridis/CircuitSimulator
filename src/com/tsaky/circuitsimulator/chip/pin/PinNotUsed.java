package com.tsaky.circuitsimulator.chip.pin;

public class PinNotUsed extends Pin{

    public PinNotUsed(String pinName, int pinID) {
        super(pinName, pinID);
    }

    @Override
    public String toString() {
        return "PinNotUsed{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + "'}";
    }
}
