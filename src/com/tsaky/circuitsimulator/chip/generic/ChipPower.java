package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipPower extends Chip {

    public ChipPower() {
        super("power", "Power",
                new Pin[]{
                        new Pin("Vcc", 0, PinType.POWER_SOURCE)
        });

        setSizeWithoutPins(34, 20);
    }

    @Override
    public String getDescription() {
        return "Positive voltage source";
    }

    @Override
    public void calculate() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        int x = getPosX() - getWidth() / 2;
        int y = getPosY() - getHeight() / 2;
        Color c = g.getColor();

        g.setColor(isSelected() ? Color.RED : Color.GREEN.darker());
        g.fillRect(x + offsetX, y + offsetY, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paint(g,offsetX, offsetY, "Vcc", pinDescription, false);
        g.setColor(c);

    }
}
