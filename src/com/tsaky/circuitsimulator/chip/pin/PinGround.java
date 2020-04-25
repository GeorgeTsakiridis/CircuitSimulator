package com.tsaky.circuitsimulator.chip.pin;

public class PinGround extends Pin{
    private boolean isGrounded = false;
    private boolean isGround = false;

    public PinGround(String pinName, int pinID) {
        super(pinName, pinID);
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public boolean canConnectTo(Pin pin) {
        return pin instanceof PinGround;
    }

    @Override
    public String toString() {
        return "PinGround{" +
                "pinID=" + getPinID() +
                ", pinName='" + getName() + '\'' +
                ", isGrounded=" + isGrounded +
                '}';
    }

}
