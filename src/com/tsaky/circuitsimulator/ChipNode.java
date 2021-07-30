package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinInput;

import java.awt.*;

public class ChipNode extends Chip {

    public ChipNode() {
        super("Node", new InfoPage("A node used to organize the connection wires."),
                new Pin[]{
                        new PinInput("input", 0),
                });

        setSizeWithoutPins(6, 6);
    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() + offsetX - getWidth() / 2;
        int y = getPosY() + offsetY - getHeight() / 2;

        g.fillRect(getPosX()+offsetX-3, getPosY()+offsetY-3, 6, 6);

        getPin(0).setBounds(getPosX()-3, getPosY()-3, getWidth()+1, getHeight()+1);
        getPin(0).paintWithPinName(g, x-1, y-1, getWidth()+2, "");
    }

}
