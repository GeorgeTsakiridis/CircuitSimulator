package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinGround;

import java.awt.*;

public class ChipGround extends Chip {

    public ChipGround() {
        super("0V",
                new InfoPage("Ground."),
                new Pin[]{
                new PinGround("0V", 0, true)
        });

        setSizeWithoutPins(20, 20);
    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() + offsetX - getWidth() / 2;
        int y = getPosY() + offsetY - getHeight() / 2;
        Color c = g.getColor();

        g.setColor(Color.GREEN.darker());
        g.fillRect(x, y, getWidth(), getHeight());
        g.setColor(c);
        getPin(0).paintWithPinName(g, x, y, getWidth(), "0V");
    }
}
