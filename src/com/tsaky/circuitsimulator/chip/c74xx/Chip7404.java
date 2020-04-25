package com.tsaky.circuitsimulator.chip.c74xx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7404 extends Chip {

    public Chip7404() {
        super("7404",
                new InfoPage("Hex Inverter IC", "ic7404.png"),
                new Pin[]{
        new PinInput("1A", 0),
        new PinOutput("1Y", 1),
        new PinInput("2A", 2),
        new PinOutput("2Y", 3),
        new PinInput("3A", 4),
        new PinOutput("3Y", 5),
        new PinGround("GND", 6),
        new PinOutput("4Y", 7),
        new PinInput("4A", 8),
        new PinOutput("5Y", 9),
        new PinInput("5A", 10),
        new PinOutput("6Y", 11),
        new PinInput("6A", 12),
        new PinPower("VCC", 13)});

        setSize(40, 180);

    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
        }else{
            getOutputPin(1).setHigh(!getInputPin(0).isLinkHigh());
            getOutputPin(3).setHigh(!getInputPin(2).isLinkHigh());
            getOutputPin(5).setHigh(!getInputPin(4).isLinkHigh());
            getOutputPin(7).setHigh(!getInputPin(8).isLinkHigh());
            getOutputPin(9).setHigh(!getInputPin(10).isLinkHigh());
            getOutputPin(11).setHigh(!getInputPin(12).isLinkHigh());
        }
    }

}
