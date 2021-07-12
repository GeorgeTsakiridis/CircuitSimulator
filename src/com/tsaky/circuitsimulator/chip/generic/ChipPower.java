package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinPower;

import java.awt.*;

public class ChipPower extends Chip {

    public ChipPower() {
        super("+V", new InfoPage("Positive voltage source."),
                new Pin[]{
                        new PinPower("+V", 0, true)
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
        getPin(0).paintWithPinName(g, x, y, getWidth(), "+V");
    }
}
