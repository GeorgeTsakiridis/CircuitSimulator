package eu.gtsak.circuitsimulator.chip.generic;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;

import java.awt.*;

public class ChipPowerSwitch extends Chip {

    public ChipPowerSwitch() {
        super("power_switch", Localization.getString("power_switch_name"),
                new Pin[]{new Pin("OUT", 0, PinType.GROUND_SOURCE)});
        setSizeWithoutPins(34, 20);
    }

    @Override
    public String getDescription() {
        return Localization.getString("power_switch_description");
    }

    @Override
    public void toggle() {
        Pin pin = getPin(0);

        if(pin.getType() == PinType.GROUND_SOURCE){
            pin.setType(PinType.POWER_SOURCE);
        }else{
            pin.setType(PinType.GROUND_SOURCE);
        }
    }

    @Override
    public void calculate() {

    }

    @Override
    public byte[] getExtraDataBytes() {
        byte b;

        if((getPin(0)).getType() == PinType.POWER_SOURCE){
            b = 1;
        }
        else{
            b = 0;
        }

        return new byte[]{b};
    }

    @Override
    public void setExtraData(byte[] bytes) {
        (getPin(0)).setType(bytes[0] == (byte)1 ? PinType.POWER_SOURCE : PinType.GROUND_SOURCE);
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        int x = getPosX() - getWidth() / 2;
        int y = getPosY() - getHeight() / 2;
        Color c = g.getColor();
        String text = Localization.getString("off");
        if(getPin(0).getType() == PinType.POWER_SOURCE){
            g.setColor(Color.GREEN.darker());
            text= Localization.getString("on");
        }else{
            g.setColor(Color.RED.darker());
        }
        g.fillRect(x + offsetX, y + offsetY, getWidth(), getHeight());
        g.setColor(c);
        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paint(g, offsetX, offsetY, text, pinDescription, false);
    }
}
