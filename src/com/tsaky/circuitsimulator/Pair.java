package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.pin.Pin;

public class Pair {

    private final Pin pin1;
    private final Pin pin2;
    private boolean removable = true;

    public Pair(Pin pin1, Pin pin2){
        this.pin1 = pin1;
        this.pin2 = pin2;
    }

    public Pair(Pin pin1, Pin pin2, boolean removable){
        this(pin1, pin2);
        this.removable = removable;
    }

    public Pin getPin1() {
        return pin1;
    }

    public Pin getPin2() {
        return pin2;
    }

    public Pin getOtherPin(Pin pin){
        if(!contains(pin))return null;

        return pin == pin1 ? pin2 : pin1;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean contains(Pin pin){
        return pin == pin1 || pin == pin2;
    }

    public boolean equals(Pair pair){
        return pin1 == pair.getPin1() && pin2 == pair.getPin2() || pin1 == pair.getPin2() && pin2 == pair.getPin1();
    }
}
