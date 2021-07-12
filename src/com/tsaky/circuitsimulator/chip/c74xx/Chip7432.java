package com.tsaky.circuitsimulator.chip.c74xx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7432 extends Chip {

    public Chip7432(){
        super("7432",
                new InfoPage("Quad 2-Input Positive OR Gates IC", "ic7432.png"),
                new Pin[]{
                new PinInput("1A", 0),
                new PinInput("1B", 1),
                new PinOutput("1Y", 2),
                new PinInput("2A", 3),
                new PinInput("2B", 4),
                new PinOutput("2Y", 5),
                new PinGround("GND", 6),
                new PinOutput("3Y", 7),
                new PinInput("3A", 8),
                new PinInput("3B", 9),
                new PinOutput("4Y", 10),
                new PinInput("4A", 11),
                new PinInput("4B", 12),
                new PinPower("VCC", 13)
        });
        setSize(40, 180);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
            turnAllOutputsToHighZ();
        }else{
            getOutputPin(2).setHigh(getInputPin(0).isLinkHigh() || getInputPin(1).isLinkHigh());
            getOutputPin(5).setHigh(getInputPin(3).isLinkHigh() || getInputPin(4).isLinkHigh());
            getOutputPin(7).setHigh(getInputPin(8).isLinkHigh() || getInputPin(9).isLinkHigh());
            getOutputPin(10).setHigh(getInputPin(11).isLinkHigh() || getInputPin(12).isLinkHigh());
        }
    }

}
