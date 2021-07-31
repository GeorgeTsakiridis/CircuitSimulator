package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;

import java.awt.*;

public class ChipPowerSwitch extends Chip {

    public ChipPowerSwitch() {
        super("Power Switch",
                new InfoPage("A Simple Power Switch. Acts as a voltage source when active. Can be toggled."),
                new Pin[]{new PinOutput("OUT", 0)});
        setSizeWithoutPins(20, 20);
    }

    @Override
    public void toggle() {
        PinOutput pinOutput = (PinOutput)getPin(0);
        pinOutput.setHigh(!pinOutput.isHigh());
    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public byte[] getExtraDataBytes() {
        byte b;

        if(((PinOutput)getPin(0)).isHigh()){
            b = 1;
        }
        else{
            b = 0;
        }

        return new byte[]{b};
    }

    @Override
    public void setExtraData(byte[] bytes) {
        ((PinOutput)getPin(0)).setHigh(bytes[0] == (byte)1);
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() - getWidth() / 2;
        int y = getPosY() - getHeight() / 2;
        Color c = g.getColor();
        String text = "OFF";
        if((getOutputPin(0)).isHigh()){
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
