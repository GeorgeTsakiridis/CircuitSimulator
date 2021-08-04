package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7402 extends Chip {

    public Chip7402(){
        super("7402", "NOR Gates IC",
                new Pin[]{
                new Pin("1Y", 0, PinType.OUTPUT),
                new Pin("1A", 1, PinType.INPUT),
                new Pin("1B", 2, PinType.INPUT),
                new Pin("2Y", 3, PinType.OUTPUT),
                new Pin("2A", 4, PinType.INPUT),
                new Pin("2B", 5, PinType.INPUT),
                new Pin("GND", 6, PinType.GROUND),
                new Pin("3A", 7, PinType.INPUT),
                new Pin("3B", 8, PinType.INPUT),
                new Pin("3Y", 9, PinType.OUTPUT),
                new Pin("4A", 10, PinType.INPUT),
                new Pin("4B", 11, PinType.INPUT),
                new Pin("4Y", 12, PinType.OUTPUT),
                new Pin("VCC", 13, PinType.POWER)
        });
        setSize(40, 180);
    }

    @Override
    public String getDescription() {
        return "Quad 2-Input NOR Gate IC";
    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            getPin(0).setHigh(!getPin(1).isLinkHigh() && !getPin(2).isLinkHigh());
            getPin(3).setHigh(!getPin(4).isLinkHigh() && !getPin(5).isLinkHigh());
            getPin(9).setHigh(!getPin(8).isLinkHigh() && !getPin(7).isLinkHigh());
            getPin(12).setHigh(!getPin(11).isLinkHigh() && !getPin(10).isLinkHigh());
        }
    }

}
