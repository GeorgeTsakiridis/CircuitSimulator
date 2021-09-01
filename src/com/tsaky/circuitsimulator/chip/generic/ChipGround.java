package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.Localization;

import java.awt.*;

public class ChipGround extends Chip {

    public ChipGround() {
        super("ground", Localization.getString("ground_name"),
                new Pin[]{
                new Pin("GND", 0, PinType.GROUND_SOURCE)
        });

        setSizeWithoutPins(34, 20);
    }

    @Override
    public String getDescription() {
        return Localization.getString("ground_description");
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
        getPin(0).paint(g, offsetX, offsetY, "GND", pinDescription, false);
        g.setColor(c);
    }
}
