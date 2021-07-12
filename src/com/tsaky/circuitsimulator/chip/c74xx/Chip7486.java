package com.tsaky.circuitsimulator.chip.c74xx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7486 extends Chip {

    public Chip7486(){
        super("7486",
                new InfoPage("Quad 2-input XOR gate IC", "ic7486.png"),
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
            boolean a1 = getInputPin(0).isLinkHigh();
            boolean a2 = getInputPin(1).isLinkHigh();
            boolean b1 = getInputPin(3).isLinkHigh();
            boolean b2 = getInputPin(4).isLinkHigh();
            boolean c1 = getInputPin(8).isLinkHigh();
            boolean c2 = getInputPin(9).isLinkHigh();
            boolean d1 = getInputPin(11).isLinkHigh();
            boolean d2 = getInputPin(12).isLinkHigh();

            getOutputPin(2).setHigh((a1 && !a2) || (!a1 && a2));
            getOutputPin(5).setHigh((b1 && !b2) || (!b1 && b2));
            getOutputPin(7).setHigh((c1 && !c2) || (!c1 && c2));
            getOutputPin(10).setHigh((d1 && !d2) || (!d1 && d2));
        }
    }

}
