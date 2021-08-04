package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipPowerSwitch extends Chip {

    public ChipPowerSwitch() {
        super("power_switch", "Power Switch",
                new Pin[]{new Pin("OUT", 0, PinType.GROUND_SOURCE)});
        setSizeWithoutPins(34, 20);
    }

    @Override
    public String getDescription() {
        return "A Simple Power Switch. Acts as a voltage source when active. Can be toggled.";
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
        String text = "OFF";
        if(getPin(0).getType() == PinType.POWER_SOURCE){
            g.setColor(Color.GREEN.darker());
            text= "ON";
        }else{
            g.setColor(Color.RED.darker());
        }
        g.fillRect(x + offsetX, y + offsetY, getWidth(), getHeight());
        g.setColor(c);
        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paint(g, offsetX, offsetY, text, pinDescription, false);
    }
}
