package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.Linker;

public class PinPower extends Pin{
    private final boolean isPowerSource;

    public PinPower(String pinName, int pinID, boolean isPowerSource){
        super(pinName, pinID);
        this.isPowerSource = isPowerSource;
    }

    public PinPower(String pinName, int pinID) {
        this(pinName, pinID, false);
    }

    public boolean isPowerSource() {
        return isPowerSource;
    }

    public boolean isPowered() {

        if(isPowerSource)return true;

        for(Pin pin : Linker.getAllConnectedPinsWith(this, null)){
            if(pin instanceof PinPower && ((PinPower)pin).isPowerSource || pin instanceof PinOutput && ((PinOutput) pin).isHigh())return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "PinPower{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + '\'' +
                ", isPowered=" + isPowered() +
                '}';
    }
}
