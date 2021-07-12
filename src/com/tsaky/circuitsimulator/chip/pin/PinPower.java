package com.tsaky.circuitsimulator.chip.pin;

public class PinPower extends Pin{
    private final boolean isPowerSource;

    public PinPower(String pinName, int pinID, boolean isPowerSource){
        super(pinName, pinID);
        this.isPowerSource = isPowerSource;
    }

    public PinPower(String pinName, int pinID) {
        this(pinName, pinID, false);
    }

    public boolean isPowered() {

        if(isPowerSource)return true;

        if(hasLink()){
            for(Pin pin : getLink().getPins()){
                if(pin instanceof PinPower && ((PinPower)pin).isPowerSource)return true;
            }
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
