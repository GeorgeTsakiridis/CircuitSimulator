package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip74283 extends Chip {

    public Chip74283(){
        super("74283",
                new InfoPage("4-bit Binary Full Adder (has carry in function)", "ic74283.png"),
                new Pin[]{
                new Pin("S2", 0, PinType.OUTPUT),
                new Pin("B2", 1, PinType.INPUT),
                new Pin("A2", 2, PinType.INPUT),
                new Pin("S1", 3, PinType.OUTPUT),
                new Pin("A1", 4, PinType.INPUT),
                new Pin("B1", 5, PinType.INPUT),
                new Pin("C0", 6, PinType.INPUT),
                new Pin("GND", 7, PinType.GROUND),
                new Pin("C4", 8, PinType.OUTPUT),
                new Pin("S4", 9, PinType.OUTPUT),
                new Pin("B4", 10, PinType.INPUT),
                new Pin("A4", 11, PinType.INPUT),
                new Pin("S3", 12, PinType.OUTPUT),
                new Pin("A3", 13, PinType.INPUT),
                new Pin("B3", 14, PinType.INPUT),
                new Pin("VCC", 15, PinType.POWER)
        });
        setSize(40, 200);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);            
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);

            int sum = 0;
            sum += getPin(6).isLinkHigh() ? 1 : 0;
            sum += getPin(4).isLinkHigh() ? 1 : 0;
            sum += getPin(5).isLinkHigh() ? 1 : 0;
            sum += getPin(1).isLinkHigh() ? 2 : 0;
            sum += getPin(2).isLinkHigh() ? 2 : 0;
            sum += getPin(13).isLinkHigh() ? 4 : 0;
            sum += getPin(14).isLinkHigh() ? 4 : 0;
            sum += getPin(10).isLinkHigh() ? 8 : 0;
            sum += getPin(11).isLinkHigh() ? 8 : 0;

            turnAllOutputsOff();

            if(sum >= 16){
                getPin(8).setHigh(true);
                sum -= 16;
            }
            if(sum >= 8){
                getPin(9).setHigh(true);
                sum -= 8;
            }
            if(sum >= 4){
                getPin(12).setHigh(true);
                sum -= 4;
            }
            if(sum >= 2){
                getPin(0).setHigh(true);
                sum -= 2;
            }
            if(sum >= 1){
                getPin(3).setHigh(true);
            }
        }
    }

}
