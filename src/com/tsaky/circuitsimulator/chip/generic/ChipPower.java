package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinPower;

import java.awt.*;

public class ChipPower extends Chip {

    public ChipPower() {
        super("Power", new InfoPage("Positive voltage source."),
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
        int x = getPosX() - getWidth() / 2;
        int y = getPosY() - getHeight() / 2;
        Color c = g.getColor();

        g.setColor(isSelected() ? Color.RED : Color.GREEN.darker());
        g.fillRect(x + offsetX, y + offsetY, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paintWithPinName(g, x + offsetX, y + offsetY, getWidth(), "+V");
        g.setColor(c);

    }
}
