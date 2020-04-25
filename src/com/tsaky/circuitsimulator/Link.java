package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Link {

    private ArrayList<Pin> pins = new ArrayList<>();
    private Linker linker;
    private boolean lastIsLineHigh = false;

    Color c;
    public Link(Linker linker){
        this.linker = linker;

        Random r = new Random();
        switch (r.nextInt(9)){
            default:
            case 0:c = Color.BLACK; break;
            case 1:c = Color.RED; break;
            case 2:c = Color.GREEN; break;
            case 3:c = Color.CYAN; break;
            case 4:c = Color.ORANGE; break;
            case 5:c = Color.BLUE; break;
            case 6:c = Color.MAGENTA; break;
            case 7:c = Color.PINK; break;
            case 8:c = Color.DARK_GRAY; break;
        }
    }

    public void addPin(Pin pin){
        if(!pins.contains(pin)) {
            pin.setLink(this);
            pins.add(pin);
        };
    }

    public void removePin(Pin pin){
        pin.setLink(null);
        pins.remove(pin);
    }

    public void removeAll(){
        for(Pin pin : pins){
            pin.setLink(null);
        }
        pins.clear();
    }

    public boolean contains(Pin pin){
        return pin.getLink() == this;
    }

    public int size(){
        return pins.size();
    }

    public ArrayList<Pin> getPins(){
        return pins;
    }

    public boolean checkAndRemoveShorts() {
        boolean first = true;
        boolean isHigh = false;

        for (Pin pin : pins) {
            if (pin instanceof PinOutput) {
                PinOutput pinOutput = (PinOutput) pin;
                if (pinOutput.isHighZMode()) continue;

                if (first) {
                    first = false;
                    isHigh = pinOutput.isHigh();
                } else {
                    if (isHigh != pinOutput.isHigh()) {
                        //shortedPins.add(pinOutput);
                        Handler.SHORTED = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isLineHigh() {
        if(!Handler.EMULATION_RUNNING)return lastIsLineHigh;

        if(checkAndRemoveShorts())return lastIsLineHigh;

        for(Pin pin : pins){
            if(pin instanceof PinOutput && !((PinOutput) pin).isHighZMode()){
                if(((PinOutput) pin).isHigh()){
                    lastIsLineHigh = true;
                    return true;
                }
            }
        }

        lastIsLineHigh = false;
        return false;
    }
}
