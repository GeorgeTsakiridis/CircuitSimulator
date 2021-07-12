package com.tsaky.circuitsimulator.chip.c74xxx;

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
                        new PinInput("OE", 0),
                        new PinInput("1D", 1),
                        new PinInput("2D", 2),
                        new PinInput("3D", 3),
                        new PinInput("4D", 4),
                        new PinInput("5D", 5),
                        new PinInput("6D", 6),
                        new PinInput("7D", 7),
                        new PinInput("8D", 8),
                        new PinGround("GND", 9),
                        new PinInput("CLK", 10),
                        new PinOutput("8Q", 11),
                        new PinOutput("7Q", 12),
                        new PinOutput("6Q", 13),
                        new PinOutput("5Q", 14),
                        new PinOutput("4Q", 15),
                        new PinOutput("3Q", 16),
                        new PinOutput("2Q", 17),
                        new PinOutput("1Q", 18),
                        new PinPower("VCC", 19)
                });
        setSize(40, 220);
    }

    @Override
    public void calculateOutputs() {
        if (!isPowered()) {
            turnAllOutputsOff();
            turnAllOutputsToHighZ();
        } else {
            boolean OE = getInputPin(0).isLinkHigh();
            boolean CLK = getInputPin(10).isLinkHigh();
            if (!lastCLK && CLK) {
                s0 = getInputPin(1).isLinkHigh();
                s1 = getInputPin(2).isLinkHigh();
                s2 = getInputPin(3).isLinkHigh();
                s3 = getInputPin(4).isLinkHigh();
                s4 = getInputPin(5).isLinkHigh();
                s5 = getInputPin(6).isLinkHigh();
                s6 = getInputPin(7).isLinkHigh();
                s7 = getInputPin(8).isLinkHigh();
            }
            lastCLK = CLK;
            if (!OE) {
                getOutputPin(11).setHigh(s7);
                getOutputPin(12).setHigh(s6);
                getOutputPin(13).setHigh(s5);
                getOutputPin(14).setHigh(s4);
                getOutputPin(15).setHigh(s3);
                getOutputPin(16).setHigh(s2);
                getOutputPin(17).setHigh(s1);
                getOutputPin(18).setHigh(s0);
            } else {
                turnAllOutputsToHighZ();
            }
        }
    }
}
