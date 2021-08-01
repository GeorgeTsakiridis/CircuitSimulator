package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7486 extends Chip {

    public Chip7486() {
        super("7486", "XOR Gates IC",
                new InfoPage("Quad 2-input XOR gate IC", "ic7486.png"),
                new Pin[]{
                        new Pin("1A", 0, PinType.INPUT),
                        new Pin("1B", 1, PinType.INPUT),
                        new Pin("1Y", 2, PinType.OUTPUT),
                        new Pin("2A", 3, PinType.INPUT),
                        new Pin("2B", 4, PinType.INPUT),
                        new Pin("2Y", 5, PinType.OUTPUT),
                        new Pin("GND", 6, PinType.GROUND),
                        new Pin("3Y", 7, PinType.OUTPUT),
                        new Pin("3A", 8, PinType.INPUT),
                        new Pin("3B", 9, PinType.INPUT),
                        new Pin("4Y", 10, PinType.OUTPUT),
                        new Pin("4A", 11, PinType.INPUT),
                        new Pin("4B", 12, PinType.INPUT),
                        new Pin("VCC", 13, PinType.POWER)
                });
        setSize(40, 180);
    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            boolean a1 = getPin(0).isLinkHigh();
            boolean a2 = getPin(1).isLinkHigh();
            boolean b1 = getPin(3).isLinkHigh();
            boolean b2 = getPin(4).isLinkHigh();
            boolean c1 = getPin(8).isLinkHigh();
            boolean c2 = getPin(9).isLinkHigh();
            boolean d1 = getPin(11).isLinkHigh();
            boolean d2 = getPin(12).isLinkHigh();

            getPin(2).setHigh((a1 && !a2) || (!a1 && a2));
            getPin(5).setHigh((b1 && !b2) || (!b1 && b2));
            getPin(7).setHigh((c1 && !c2) || (!c1 && c2));
            getPin(10).setHigh((d1 && !d2) || (!d1 && d2));
        }
    }

}
