package com.tsaky.circuitsimulator.chip.c74xxx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip74283 extends Chip {

    public Chip74283(){
        super("74283",
                new InfoPage("4-bit Binary Full Adder (has carry in function)", "ic74283.png"),
                new Pin[]{
                new PinOutput("S2", 0),
                new PinInput("B2", 1),
                new PinInput("A2", 2),
                new PinOutput("S1", 3),
                new PinInput("A1", 4),
                new PinInput("B1", 5),
                new PinInput("C0", 6),
                new PinGround("GND", 7),
                new PinOutput("C4", 8),
                new PinOutput("S4", 9),
                new PinInput("B4", 10),
                new PinInput("A4", 11),
                new PinOutput("S3", 12),
                new PinInput("A3", 13),
                new PinInput("B3", 14),
                new PinPower("VCC", 15)
        });
        setSize(40, 200);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
        }else{
            //pin
            int sum = 0;
            sum += getInputPin(6).isLinkHigh() ? 1 : 0;
            sum += getInputPin(4).isLinkHigh() ? 1 : 0;
            sum += getInputPin(5).isLinkHigh() ? 1 : 0;
            sum += getInputPin(1).isLinkHigh() ? 2 : 0;
            sum += getInputPin(2).isLinkHigh() ? 2 : 0;
            sum += getInputPin(13).isLinkHigh() ? 4 : 0;
            sum += getInputPin(14).isLinkHigh() ? 4 : 0;
            sum += getInputPin(10).isLinkHigh() ? 8 : 0;
            sum += getInputPin(11).isLinkHigh() ? 8 : 0;

            turnAllOutputsOff();

            if(sum >= 16){
                getOutputPin(8).setHigh(true);
                sum -= 16;
            }
            if(sum >= 8){
                getOutputPin(9).setHigh(true);
                sum -= 8;
            }
            if(sum >= 4){
                getOutputPin(12).setHigh(true);
                sum -= 4;
            }
            if(sum >= 2){
                getOutputPin(0).setHigh(true);
                sum -= 2;
            }
            if(sum >= 1){
                getOutputPin(3).setHigh(true);
            }
        }
    }

}
