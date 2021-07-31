package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;

import java.awt.*;

public class ChipNode extends Chip {

    public ChipNode() {
        super("Node", new InfoPage("A node used to organize the connection wires."),
                new Pin[]{
                        new Pin("node", 0, PinType.NOT_USED),
                });

        setSizeWithoutPins(6, 6);
    }

    @Override
    public void calculate() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        g.fillRect(getPosX()+offsetX-3, getPosY()+offsetY-3, 6, 6);

        getPin(0).setBounds(getPosX()-3, getPosY()-3, getWidth(), getHeight());
        getPin(0).paint(g, offsetX, offsetY, "");
    }

}
