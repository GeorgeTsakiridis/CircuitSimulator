package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

import java.awt.*;

public class ChipRelay extends Chip {

    public ChipRelay() {
        super("Relay", new InfoPage("A Dual Relay"), new Pin[]{
                new Pin("+", 0, PinType.POWER),
                new Pin("GND", 1, PinType.GROUND),
                new Pin("1C", 2, PinType.NOT_USED),
                new Pin("1NC", 3, PinType.NOT_USED),
                new Pin("1NO", 4, PinType.NOT_USED),
                new Pin("2C", 5, PinType.NOT_USED),
                new Pin("2NC", 6, PinType.NOT_USED),
                new Pin("2NO", 7, PinType.NOT_USED),
        });

        setSizeWithoutPins(120, 60);
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
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        g.drawRect(getPosX()-getWidth()/2 + offsetX, getPosY()-getHeight()/2 + offsetY, getWidth(), getHeight());

        getPin(0).setBounds(getPosX()-getWidth()/2+3, getPosY()-getHeight()/2+3, 14, 14);
        getPin(0).paint(g, offsetX, offsetY, "+");

        getPin(1).setBounds(getPosX()-getWidth()/2+3, getPosY()+getHeight()/2-17, 14, 14);
        getPin(1).paint(g, offsetX, offsetY, "-");

        int x = getPosX() + 19;
        int y = getPosY()-getHeight()/2+3;
        int y2 = getPosY()+getHeight()/2-17;

        getPin(2).setBounds(x, y, 14, 14);
        getPin(2).paint(g, offsetX, offsetY);
        getPin(3).setBounds(x-24, y, 14, 14);
        getPin(3).paint(g, offsetX, offsetY);
        getPin(4).setBounds(x+24, y, 14, 14);
        getPin(4).paint(g, offsetX, offsetY);

        getPin(5).setBounds(x, y2, 14, 14);
        getPin(5).paint(g, offsetX, offsetY);
        getPin(6).setBounds(x-24, y2, 14, 14);
        getPin(6).paint(g, offsetX, offsetY);
        getPin(7).setBounds(x+24, y2, 14, 14);
        getPin(7).paint(g, offsetX, offsetY);

        g.drawString("Relay", getPosX() + offsetX + 8, getPosY() + offsetY + g.getFont().getSize()/2-1);

        if(isPowered()){
            g.drawString("Active", getPosX() + offsetX - 40, getPosY() + offsetY + g.getFont().getSize()/2-1);
        }

    }
}
