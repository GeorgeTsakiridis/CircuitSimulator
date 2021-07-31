package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip74574 extends Chip {

    private boolean s0 = false;
    private boolean s1 = false;
    private boolean s2 = false;
    private boolean s3 = false;
    private boolean s4 = false;
    private boolean s5 = false;
    private boolean s6 = false;
    private boolean s7 = false;

    private boolean lastCLK = false;

    public Chip74574() {
        super("74574",
                new InfoPage("Octal D-type edge-triggered flip-flop", "ic74574.png"),
                new Pin[]{
                        new Pin("OE", 0, PinType.INPUT),
                        new Pin("1D", 1, PinType.INPUT),
                        new Pin("2D", 2, PinType.INPUT),
                        new Pin("3D", 3, PinType.INPUT),
                        new Pin("4D", 4, PinType.INPUT),
                        new Pin("5D", 5, PinType.INPUT),
                        new Pin("6D", 6, PinType.INPUT),
                        new Pin("7D", 7, PinType.INPUT),
                        new Pin("8D", 8, PinType.INPUT),
                        new Pin("GND", 9, PinType.GROUND),
                        new Pin("CLK", 10, PinType.INPUT),
                        new Pin("8Q", 11, PinType.OUTPUT),
                        new Pin("7Q", 12, PinType.OUTPUT),
                        new Pin("6Q", 13, PinType.OUTPUT),
                        new Pin("5Q", 14, PinType.OUTPUT),
                        new Pin("4Q", 15, PinType.OUTPUT),
                        new Pin("3Q", 16, PinType.OUTPUT),
                        new Pin("2Q", 17, PinType.OUTPUT),
                        new Pin("1Q", 18, PinType.OUTPUT),
                        new Pin("VCC", 19, PinType.POWER)
                });
        setSize(40, 220);
    }

    @Override
    public void calculate() {
        if (!isPowered()) {
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        } else {
            boolean OE = getPin(0).isLinkHigh();
            boolean CLK = getPin(10).isLinkHigh();
            if (!lastCLK && CLK) {
                s0 = getPin(1).isLinkHigh();
                s1 = getPin(2).isLinkHigh();
                s2 = getPin(3).isLinkHigh();
                s3 = getPin(4).isLinkHigh();
                s4 = getPin(5).isLinkHigh();
                s5 = getPin(6).isLinkHigh();
                s6 = getPin(7).isLinkHigh();
                s7 = getPin(8).isLinkHigh();
            }
            lastCLK = CLK;
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
            } else {
                turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
            }
        }
    }
}
