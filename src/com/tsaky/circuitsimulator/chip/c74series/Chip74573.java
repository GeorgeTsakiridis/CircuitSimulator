package com.tsaky.circuitsimulator.chip.c74series;

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
                new Pin("OE", 0, PinType.INPUT),
                new Pin("D0", 1, PinType.INPUT),
                new Pin("D1", 2, PinType.INPUT),
                new Pin("D2", 3, PinType.INPUT),
                new Pin("D3", 4, PinType.INPUT),
                new Pin("D4", 5, PinType.INPUT),
                new Pin("D5", 6, PinType.INPUT),
                new Pin("D6", 7, PinType.INPUT),
                new Pin("D7", 8, PinType.INPUT),
                new Pin("GND", 9, PinType.GROUND),
                new Pin("LE", 10, PinType.INPUT),
                new Pin("Q7", 11, PinType.OUTPUT),
                new Pin("Q6", 12, PinType.OUTPUT),
                new Pin("Q5", 13, PinType.OUTPUT),
                new Pin("Q4", 14, PinType.OUTPUT),
                new Pin("Q3", 15, PinType.OUTPUT),
                new Pin("Q2", 16, PinType.OUTPUT),
                new Pin("Q1", 17, PinType.OUTPUT),
                new Pin("Q0", 18, PinType.OUTPUT),
                new Pin("VCC", 19, PinType.POWER)
        });
        setSize(40, 220);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else {
            boolean OE = getPin(0).isLinkHigh();
            boolean LE = getPin(10).isLinkHigh();
            if (LE) {
                s0 = getPin(1).isLinkHigh();
                s1 = getPin(2).isLinkHigh();
                s2 = getPin(3).isLinkHigh();
                s3 = getPin(4).isLinkHigh();
                s4 = getPin(5).isLinkHigh();
                s5 = getPin(6).isLinkHigh();
                s6 = getPin(7).isLinkHigh();
                s7 = getPin(8).isLinkHigh();
            }
            if (!OE) {
                turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
                getPin(11).setHigh(s7);
                getPin(12).setHigh(s6);
                getPin(13).setHigh(s5);
                getPin(14).setHigh(s4);
                getPin(15).setHigh(s3);
                getPin(16).setHigh(s2);
                getPin(17).setHigh(s1);
                getPin(18).setHigh(s0);
            }else{
                turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }
        }
    }

}
