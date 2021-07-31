package com.tsaky.circuitsimulator.chip.other;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class ChipPD43256BCZ extends Chip {

    public ChipPD43256BCZ() {
        super("PD43256BCZ", new InfoPage("A Standard SRAM with a capacity of 32K 8bit words"),
                new Pin[]{
                        new Pin("A14", 0, PinType.INPUT),
                        new Pin("A12", 1, PinType.INPUT),
                        new Pin("A7", 2, PinType.INPUT),
                        new Pin("A6", 3, PinType.INPUT),
                        new Pin("A5", 4, PinType.INPUT),
                        new Pin("A4", 5, PinType.INPUT),
                        new Pin("A3", 6, PinType.INPUT),
                        new Pin("A2", 7, PinType.INPUT),
                        new Pin("A1", 8, PinType.INPUT),
                        new Pin("A0", 9, PinType.INPUT),
                        new Pin("I/O1", 10, PinType.HIGH_Z),
                        new Pin("I/O2", 11, PinType.HIGH_Z),
                        new Pin("I/O3", 12, PinType.HIGH_Z),
                        new Pin("GND", 13, PinType.GROUND),
                        new Pin("I/O4", 14, PinType.HIGH_Z),
                        new Pin("I/O5", 15, PinType.HIGH_Z),
                        new Pin("I/O6", 16, PinType.HIGH_Z),
                        new Pin("I/O7", 17, PinType.HIGH_Z),
                        new Pin("I/O8", 18, PinType.HIGH_Z),
                        new Pin("-CS", 19, PinType.INPUT),
                        new Pin("A10", 20, PinType.INPUT),
                        new Pin("-OE", 21, PinType.INPUT),
                        new Pin("A11", 22, PinType.INPUT),
                        new Pin("A9", 23, PinType.INPUT),
                        new Pin("A8", 24, PinType.INPUT),
                        new Pin("A13", 25, PinType.INPUT),
                        new Pin("-WE", 26, PinType.INPUT),
                        new Pin("VCC", 27, PinType.POWER)
                });

        setSize(70, 350);
    }

    @Override
    public byte[] getExtraDataBytes() {
     return null;
    }

    @Override
    public void setExtraData(byte[] bytes) {
    }

    @Override
    public void calculateOutputs() {

    }

}
