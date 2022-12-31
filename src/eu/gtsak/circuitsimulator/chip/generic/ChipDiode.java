package eu.gtsak.circuitsimulator.chip.generic;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;

import java.awt.*;

public class ChipDiode extends Chip {

    public ChipDiode() {
        super("diode", Localization.getString("diode_name"), new Pin[]{
                new Pin(Localization.getString("anode"), 0, PinType.INPUT),
                new Pin(Localization.getString("cathode"), 1, PinType.OUTPUT)
        });
        setSizeWithoutPins(50, 20);
    }

    @Override
    public String getDescription() {
        return Localization.getString("diode_description");
    }

    @Override
    public void calculate() {
        if(getPin(0).isLinkHigh()){
            getPin(1).setType(PinType.OUTPUT);
            getPin(1).setHigh(true);
        }else{
            getPin(1).setType(PinType.HIGH_Z);
        }
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {

        getPin(0).setBounds(getPosX()-getWidth()/2-4, getPosY()-getHeight()/2+5, 10, 10);
        getPin(0).paint(g, offsetX, offsetY, pinDescription, true);
        getPin(1).setBounds(getPosX()+getWidth()/2-5, getPosY()-getHeight()/2+5, 10, 10);
        getPin(1).paint(g, offsetX, offsetY, pinDescription, false);

        int x = getPosX()-getWidth()/2+7 + offsetX;
        int y = getPosY() + offsetY;

        g.drawLine(x, y, x+14, y);
        g.drawLine(x+14, y-6, x+14, y+6);
        g.drawLine(x+14, y-7, x+25, y);
        g.drawLine(x+14, y+7, x+25, y);
        g.drawLine(x+26, y-6, x+26, y+6);
        g.drawLine(x+26, y, x+38, y);
    }
}
