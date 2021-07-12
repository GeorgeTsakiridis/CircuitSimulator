package com.tsaky.circuitsimulator.chip.c74xxx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip74573 extends Chip {

    private boolean s0 = false;
    private boolean s1 = false;
    private boolean s2 = false;
    private boolean s3 = false;
    private boolean s4 = false;
    private boolean s5 = false;
    private boolean s6 = false;
    private boolean s7 = false;

    public Chip74573(){
        super("74573",
                new InfoPage("Octal D-type transparent latch", "ic74573.png"),
                new Pin[]{
                new PinInput("OE", 0),
                new PinInput("D0", 1),
                new PinInput("D1", 2),
                new PinInput("D2", 3),
                new PinInput("D3", 4),
                new PinInput("D4", 5),
                new PinInput("D5", 6),
                new PinInput("D6", 7),
                new PinInput("D7", 8),
                new PinGround("GND", 9),
                new PinInput("LE", 10),
                new PinOutput("Q7", 11),
                new PinOutput("Q6", 12),
                new PinOutput("Q5", 13),
                new PinOutput("Q4", 14),
                new PinOutput("Q3", 15),
                new PinOutput("Q2", 16),
                new PinOutput("Q1", 17),
                new PinOutput("Q0", 18),
                new PinPower("VCC", 19)
        });
        setSize(40, 220);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
            turnAllOutputsToHighZ();
        }else {
            boolean OE = getInputPin(0).isLinkHigh();
            boolean LE = getInputPin(10).isLinkHigh();
            if (LE) {
                s0 = getInputPin(1).isLinkHigh();
                s1 = getInputPin(2).isLinkHigh();
                s2 = getInputPin(3).isLinkHigh();
                s3 = getInputPin(4).isLinkHigh();
                s4 = getInputPin(5).isLinkHigh();
                s5 = getInputPin(6).isLinkHigh();
                s6 = getInputPin(7).isLinkHigh();
                s7 = getInputPin(8).isLinkHigh();
            }
            if (!OE) {
                getOutputPin(11).setHigh(s7);
                getOutputPin(12).setHigh(s6);
                getOutputPin(13).setHigh(s5);
                getOutputPin(14).setHigh(s4);
                getOutputPin(15).setHigh(s3);
                getOutputPin(16).setHigh(s2);
                getOutputPin(17).setHigh(s1);
                getOutputPin(18).setHigh(s0);
            }else{
                turnAllOutputsToHighZ();
        }
        }
    }

}
