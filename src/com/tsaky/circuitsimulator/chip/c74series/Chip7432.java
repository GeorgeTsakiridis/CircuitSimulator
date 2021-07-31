package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip7432 extends Chip {

    public Chip7432() {
        super("7432",
                new InfoPage("Quad 2-Input Positive OR Gates IC", "ic7432.png"),
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
                        new Pin("VCC", 13, PinType.POWER)});
        setSize(40, 180);
    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            getPin(2).setHigh(getPin(0).isLinkHigh() || getPin(1).isLinkHigh());
            getPin(5).setHigh(getPin(3).isLinkHigh() || getPin(4).isLinkHigh());
            getPin(7).setHigh(getPin(8).isLinkHigh() || getPin(9).isLinkHigh());
            getPin(10).setHigh(getPin(11).isLinkHigh() || getPin(12).isLinkHigh());
        }
    }

}
