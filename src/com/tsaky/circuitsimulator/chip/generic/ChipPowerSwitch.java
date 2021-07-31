package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipPowerSwitch extends Chip {

    public ChipPowerSwitch() {
        super("Power Switch",
                new InfoPage("A Simple Power Switch. Acts as a voltage source when active. Can be toggled."),
                new Pin[]{new Pin("OUT", 0, PinType.GROUND_SOURCE)});
        setSizeWithoutPins(20, 20);
    }

    @Override
    public void toggle() {
        Pin pin = getPin(0);

        if(pin.getType() == PinType.GROUND_SOURCE){
            pin.setPinType(PinType.POWER_SOURCE);
        }else{
            pin.setPinType(PinType.GROUND_SOURCE);
        }
    }

    @Override
    public void calculateOutputs() {

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
        (getPin(0)).setPinType(bytes[0] == (byte)1 ? PinType.POWER_SOURCE : PinType.GROUND_SOURCE);
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
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
        getPin(0).paint(g, offsetX, offsetY, text);
    }
}
