package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinInput;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;

public class ChipSwitch extends Chip {

    public ChipSwitch() {
        super("Switch", new InfoPage("A normal 3-pin switch. Can be toggled."),
                new Pin[]{
                        new PinInput("C", 0),
                        new PinOutput("NO", 1),
                        new PinOutput("NC", 2)
                });
    }

    @Override
    public void toggle() {
        super.toggle();
    }

    @Override
    public void calculateOutputs() {
        PinInput input;
       // input.isLinkHigh()
    }
}
