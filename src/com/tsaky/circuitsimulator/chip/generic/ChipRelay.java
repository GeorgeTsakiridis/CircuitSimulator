package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.logic.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;
import com.tsaky.circuitsimulator.ui.Localization;

import java.awt.*;

public class ChipRelay extends Chip {

    public ChipRelay() {
        super("relay", Localization.getString("relay_name"),
                new Pin[]{
                new Pin("+", 0, PinType.POWER),
                new Pin("GND", 1, PinType.GROUND),
                new Pin("C2", 2, PinType.NOT_USED),
                new Pin("NC1", 3, PinType.NOT_USED),
                new Pin("NO1", 4, PinType.NOT_USED),
                new Pin("C2", 5, PinType.NOT_USED),
                new Pin("NC2", 6, PinType.NOT_USED),
                new Pin("NO2", 7, PinType.NOT_USED),
        });

        setSizeWithoutPins(120, 60);
    }

    @Override
    public String getDescription() {
        return Localization.getString("relay_description");
    }

    @Override
    public void onAdded() {
        Linker.linkPins(getPin(2), getPin(3), false);
        Linker.linkPins(getPin(5), getPin(6), false);
    }

    @Override
    public void onRemove() {
        Linker.forceUnlinkPins(getPin(2), getPin(3));
        Linker.forceUnlinkPins(getPin(2), getPin(4));
        Linker.forceUnlinkPins(getPin(5), getPin(6));
        Linker.forceUnlinkPins(getPin(5), getPin(7));
    }

    @Override
    public void calculate() {
        if(isPowered()){
            Linker.forceUnlinkPins(getPin(2), getPin(3));
            Linker.linkPins(getPin(2), getPin(4), false);
            Linker.forceUnlinkPins(getPin(5), getPin(6));
            Linker.linkPins(getPin(5), getPin(7), false);
        }else{
            Linker.forceUnlinkPins(getPin(2), getPin(4));
            Linker.linkPins(getPin(2), getPin(3), false);
            Linker.forceUnlinkPins(getPin(5), getPin(7));
            Linker.linkPins(getPin(5), getPin(6), false);
        }
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        g.drawRect(getPosX()-getWidth()/2 + offsetX, getPosY()-getHeight()/2 + offsetY, getWidth(), getHeight());

        getPin(0).setBounds(getPosX()-getWidth()/2+3, getPosY()-getHeight()/2+3, 14, 14);
        getPin(0).paint(g, offsetX, offsetY, "+", pinDescription, true);

        getPin(1).setBounds(getPosX()-getWidth()/2+3, getPosY()+getHeight()/2-17, 14, 14);
        getPin(1).paint(g, offsetX, offsetY, "-", pinDescription, true);

        int x = getPosX() + 19;
        int y = getPosY()-getHeight()/2+3;
        int y2 = getPosY()+getHeight()/2-17;

        getPin(2).setBounds(x, y, 14, 14);
        getPin(2).paint(g, offsetX, offsetY, false, false);
        getPin(3).setBounds(x-24, y, 14, 14);
        getPin(3).paint(g, offsetX, offsetY, false, false);
        getPin(4).setBounds(x+24, y, 14, 14);
        getPin(4).paint(g, offsetX, offsetY, false, false);

        getPin(5).setBounds(x, y2, 14, 14);
        getPin(5).paint(g, offsetX, offsetY, false, false);
        getPin(6).setBounds(x-24, y2, 14, 14);
        getPin(6).paint(g, offsetX, offsetY, false, false);
        getPin(7).setBounds(x+24, y2, 14, 14);
        getPin(7).paint(g, offsetX, offsetY, false, false);

        g.drawString(Localization.getString("relay_name"), getPosX() + offsetX + 8, getPosY() + offsetY + g.getFont().getSize()/2-1);

        if(isPowered()){
            g.drawString(Localization.getString("active"), getPosX() + offsetX - 40, getPosY() + offsetY + g.getFont().getSize()/2-1);
        }

        if(pinDescription){
            int dX = getPosX()+offsetX - 20;
            int dY = getPosY()+offsetY + 60;
            for(int i = 3; i < 8; i++){
                g.drawString((i+1) + ") " + getPin(i).getName(), dX, dY);
                dY += g.getFont().getSize()+5;
            }

        }

    }
}
