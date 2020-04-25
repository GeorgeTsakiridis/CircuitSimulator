package com.tsaky.circuitsimulator.chip.c74xx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7402 extends Chip {

    public Chip7402(){
        super("7402",
                new InfoPage("Quad 2-Input NOR Gate IC", "ic7402.png"),
                new Pin[]{
                new PinOutput("1Y", 0),
                new PinInput("1A", 1),
                new PinInput("1B", 2),
                new PinOutput("2Y", 3),
                new PinInput("2A", 4),
                new PinInput("2B", 5),
                new PinGround("GND", 6),
                new PinInput("3A", 7),
                new PinInput("3B", 8),
                new PinOutput("3Y", 9),
                new PinInput("4A", 10),
                new PinInput("4B", 11),
                new PinOutput("4Y", 12),
                new PinPower("VCC", 13)
        });
        setSize(40, 180);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
        }else{
            getOutputPin(0).setHigh(!getInputPin(1).isLinkHigh() && !getInputPin(2).isLinkHigh());
            //getOutputPin(0).setHigh(!getInputPin(1).isHigh() && !getInputPin(2).isHigh());
            //getOutputPin(3).setHigh(!getInputPin(4).isHigh() && !getInputPin(5).isHigh());
            //getOutputPin(9).setHigh(!getInputPin(7).isHigh() && !getInputPin(8).isHigh());
            //getOutputPin(12).setHigh(!getInputPin(10).isHigh() && !getInputPin(11).isHigh());
        }
    }

}
