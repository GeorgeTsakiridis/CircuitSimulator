package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.logic.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.Localization;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import java.awt.*;

public class ChipSwitch extends Chip {

    private boolean toggled = false;

    public ChipSwitch() {
        super("switch", Localization.getString("switch_name"),
                new Pin[]{
                        new Pin("C", 0, PinType.NOT_USED),
                        new Pin("NO", 1, PinType.NOT_USED),
                        new Pin("NC", 2, PinType.NOT_USED)
        });
        setSizeWithoutPins(80, 60);
    }

    @Override
    public String getDescription() {
        return Localization.getString("switch_description");
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

        String pressedText = Localization.getString("pressed");
        String switchText = Localization.getString("switch_name");

        int pressedTextWidth = (int)PaintUtils.getStringBounds(g, pressedText).getWidth();
        int switchTextWidth = (int)PaintUtils.getStringBounds(g, switchText).getWidth();

        if(toggled) {
            g.drawString(pressedText, getPosX()+offsetX-pressedTextWidth/2-1, getPosY()+offsetY-g.getFont().getSize()-1);
        }

        g.drawString(switchText, getPosX()+offsetX-switchTextWidth/2-1, getPosY()+getHeight()/2+offsetY-2);

    }
}
