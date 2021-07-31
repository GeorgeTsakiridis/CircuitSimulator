package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7404 extends Chip {

    public Chip7404() {
        super("7404",
                new InfoPage("Hex Inverter IC", "ic7404.png"),
                new Pin[]{
        new Pin("1A", 0, PinType.INPUT),
        new Pin("1Y", 1, PinType.OUTPUT),
        new Pin("2A", 2, PinType.INPUT),
        new Pin("2Y", 3, PinType.OUTPUT),
        new Pin("3A", 4, PinType.INPUT),
        new Pin("3Y", 5, PinType.OUTPUT),
        new Pin("GND", 6, PinType.GROUND),
        new Pin("4Y", 7, PinType.OUTPUT),
        new Pin("4A", 8, PinType.INPUT),
        new Pin("5Y", 9, PinType.OUTPUT),
        new Pin("5A", 10, PinType.INPUT),
        new Pin("6Y", 11, PinType.OUTPUT),
        new Pin("6A", 12, PinType.INPUT),
        new Pin("VCC", 13, PinType.POWER)});

        setSize(40, 180);

    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            getPin(1).setHigh(!getPin(0).isLinkHigh());
            getPin(3).setHigh(!getPin(2).isLinkHigh());
            getPin(5).setHigh(!getPin(4).isLinkHigh());
            getPin(7).setHigh(!getPin(8).isLinkHigh());
            getPin(9).setHigh(!getPin(10).isLinkHigh());
            getPin(11).setHigh(!getPin(12).isLinkHigh());
        }
    }

}
