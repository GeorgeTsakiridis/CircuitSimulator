package com.tsaky.circuitsimulator.chip.c74xxx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip74257 extends Chip {

    public Chip74257(){
        super("74283",
                new InfoPage("Quad 2-Line to 1-Line Data Selectors", "ic74257.png"),
                new Pin[]{
                new PinInput("A/B", 0),
                new PinInput("1A", 1),
                new PinInput("1B", 2),
                new PinOutput("1Y", 3),
                new PinInput("2A", 4),
                new PinInput("2B", 5),
                new PinOutput("2Y", 6),
                new PinGround("GND", 7),
                new PinOutput("3Y", 8),
                new PinInput("3B", 9),
                new PinInput("3A", 10),
                new PinOutput("4Y", 11),
                new PinInput("4B", 12),
                new PinInput("4A", 13),
                new PinInput("G", 14),
                new PinPower("VCC", 15)
        });
        setSize(40, 200);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
            turnAllOutputsToHighZ();
        }else{
            turnAllOutputsOff();

            boolean G = getInputPin(14).isLinkHigh();
            boolean AB = getInputPin(0).isLinkHigh();

            if(!G){
                boolean d1, d2, d3, d4;
                if(!AB){
                    d1 = getInputPin(1).isLinkHigh();
                    d2 = getInputPin(4).isLinkHigh();
                    d3 = getInputPin(10).isLinkHigh();
                    d4 = getInputPin(13).isLinkHigh();
                }else{
                    d1 = getInputPin(2).isLinkHigh();
                    d2 = getInputPin(5).isLinkHigh();
                    d3 = getInputPin(9).isLinkHigh();
                    d4 = getInputPin(12).isLinkHigh();
                }

                getOutputPin(3).setHigh(d1);
                getOutputPin(6).setHigh(d2);
                getOutputPin(8).setHigh(d3);
                getOutputPin(11).setHigh(d4);
            }

        }
    }

}
