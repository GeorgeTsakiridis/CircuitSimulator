package eu.gtsak.circuitsimulator.chip.c74series;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;

public class Chip74257 extends Chip {

    public Chip74257(){
        super("74257", Localization.getString("chip74257_name"),
                new Pin[]{
                new Pin("A/B", 0, PinType.INPUT),
                new Pin("1A", 1, PinType.INPUT),
                new Pin("1B", 2, PinType.INPUT),
                new Pin("1Y", 3, PinType.OUTPUT),
                new Pin("2A", 4, PinType.INPUT),
                new Pin("2B", 5, PinType.INPUT),
                new Pin("2Y", 6, PinType.OUTPUT),
                new Pin("GND", 7, PinType.GROUND),
                new Pin("3Y", 8, PinType.OUTPUT),
                new Pin("3B", 9, PinType.INPUT),
                new Pin("3A", 10, PinType.INPUT),
                new Pin("4Y", 11, PinType.OUTPUT),
                new Pin("4B", 12, PinType.INPUT),
                new Pin("4A", 13, PinType.INPUT),
                new Pin("-OE", 14, PinType.INPUT),
                new Pin("VCC", 15, PinType.POWER)
        });
        setSize(40, 200);
    }

    @Override
    public String getDescription() {
        return Localization.getString("chip74257_description");
    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            turnAllOutputsOff();

            boolean G = getPin(14).isLinkHigh();
            boolean AB = getPin(0).isLinkHigh();

            if(!G){
                boolean d1, d2, d3, d4;
                if(!AB){
                    d1 = getPin(1).isLinkHigh();
                    d2 = getPin(4).isLinkHigh();
                    d3 = getPin(10).isLinkHigh();
                    d4 = getPin(13).isLinkHigh();
                }else{
                    d1 = getPin(2).isLinkHigh();
                    d2 = getPin(5).isLinkHigh();
                    d3 = getPin(9).isLinkHigh();
                    d4 = getPin(12).isLinkHigh();
                }

                getPin(3).setHigh(d1);
                getPin(6).setHigh(d2);
                getPin(8).setHigh(d3);
                getPin(11).setHigh(d4);
            }

        }
    }

}
