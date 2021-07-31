package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipGround extends Chip {

    public ChipGround() {
        super("Ground",
                new InfoPage("Ground."),
                new Pin[]{
                new Pin("GND", 0, PinType.GROUND_SOURCE)
        });

        setSizeWithoutPins(20, 20);
    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() - getWidth() / 2;
        int y = getPosY() - getHeight() / 2;
        Color c = g.getColor();

        g.setColor(isSelected() ? Color.RED : Color.GREEN.darker());
        g.fillRect(x + offsetX, y + offsetY, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paint(g, offsetX, offsetY, "0V");
        g.setColor(c);
    }
}
