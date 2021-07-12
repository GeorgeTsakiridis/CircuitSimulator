package com.tsaky.circuitsimulator.chip.pin;

import com.tsaky.circuitsimulator.Linker;

public class PinGround extends Pin{
    private final boolean isGroundSource;

    public PinGround(String pinName, int pinID, boolean isGroundSource){
        super(pinName, pinID);
        this.isGroundSource = isGroundSource;
    }

    public PinGround(String pinName, int pinID) {
        this(pinName, pinID, false);
    }

    public boolean isGrounded() {

        if(isGroundSource)return true;

        for(Pin pin : Linker.getAllConnectedPinsWith(this, null)){
            if(pin instanceof PinGround && ((PinGround)pin).isGroundSource)return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "PinGround{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + '\'' +
                ", isGrounded=" + isGrounded() +
                '}';
    }

}
