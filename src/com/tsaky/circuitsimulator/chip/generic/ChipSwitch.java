package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipSwitch extends Chip {

    private boolean toggled = false;

    public ChipSwitch() {
        super("switch", "Switch",
                new Pin[]{
                        new Pin("C", 0, PinType.NOT_USED),
                        new Pin("NO", 1, PinType.NOT_USED),
                        new Pin("NC", 2, PinType.NOT_USED)
        });
        setSizeWithoutPins(75, 45);
    }

    @Override
    public String getDescription() {
        return "A normal 3-pin switch. Can be toggled.";
    }

    @Override
    public void toggle() {
        toggled = !toggled;

        if(toggled){
            Linker.forceUnlinkPins(getPin(0), getPin(2));
            Linker.linkPins(getPin(0), getPin(1), false);
        }else{
            Linker.forceUnlinkPins(getPin(0), getPin(1));
            Linker.linkPins(getPin(0), getPin(2), false);
        }

    }

    @Override
    public void onAdded() {
        Linker.linkPins(getPin(0), getPin(2), false);
    }

    @Override
    public void onRemove() {
        Linker.forceUnlinkPins(getPin(0), getPin(1));
        Linker.forceUnlinkPins(getPin(0), getPin(2));
    }

    @Override
    public void calculate() {
    }

    @Override
    public void setExtraData(byte[] bytes) {
        toggled = bytes[0] == (byte)0;
        toggle();
    }

    @Override
    public byte[] getExtraDataBytes() {
        return new byte[]{toggled ? (byte)1 : (byte)0};
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        int x = getPosX() + offsetX - getWidth()/2;
        int y = getPosY() + offsetY - getHeight()/2;

        g.drawRect(x, y, getWidth(), getHeight());

        getPin(0).setBounds(getPosX()-getWidth()/2 + 2, getPosY()-4, 10, 10);
        getPin(0).paint(g, offsetX, offsetY, pinDescription, true);

        getPin(1).setBounds(getPosX()+getWidth()/2 - 12 , getPosY()-14, 10, 10);
        getPin(1).paint(g, offsetX, offsetY, pinDescription, false);

        getPin(2).setBounds(getPosX()+getWidth()/2 - 12, getPosY()+5, 10, 10);
        getPin(2).paint(g, offsetX, offsetY, pinDescription, false);

        if(toggled) {
            g.drawString("Pressed", getPosX()+offsetX-35, getPosY()+offsetY-8);
        }

        g.drawString("Switch", getPosX()-35 + offsetX, getPosY()+20 + offsetY);

    }
}
