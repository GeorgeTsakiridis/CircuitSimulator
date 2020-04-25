package com.tsaky.circuitsimulator.chip.pin;

public class PinPower extends Pin{
    private boolean isPowered = false;

    public PinPower(String pinName, int pinID) {
        super(pinName, pinID);
    }

    public boolean isPowered() {
        return isPowered;
    }

    public void setPowered(boolean powered) {
        isPowered = powered;
    }

    @Override
    public String toString() {
        return "PinPower{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + '\'' +
                ", isPowered=" + isPowered +
                '}';
    }
}
