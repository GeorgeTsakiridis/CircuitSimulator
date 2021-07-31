package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinInput;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;

import java.awt.*;

public class ChipDiode extends Chip {

    public ChipDiode() {
        super("Diode", new InfoPage("A simple diode"), new Pin[]{
                new PinInput("Anode", 0),
                new PinOutput("Cathode", 1)
        });
        setSizeWithoutPins(50, 20);
    }

    @Override
    public void calculateOutputs() {
        if(getInputPin(0).isLinkHigh()){
            getOutputPin(1).setHighZMode(false);
            getOutputPin(1).setHigh(true);
        }else{
            getOutputPin(1).setHigh(false);
            getOutputPin(1).setHighZMode(true);
        }
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {

        getPin(0).setBounds(getPosX()-getWidth()/2-4, getPosY()-getHeight()/2+5, 10, 10);
        getPin(0).paint(g, offsetX, offsetY);
        getPin(1).setBounds(getPosX()+getWidth()/2-5, getPosY()-getHeight()/2+5, 10, 10);
        getPin(1).paint(g, offsetX, offsetY);

        int x = getPosX()-getWidth()/2+7;
        int y = getPosY();

        g.drawLine(x, y, x+14, y);
        g.drawLine(x+14, y-6, x+14, y+6);
        g.drawLine(x+14, y-7, x+25, y);
        g.drawLine(x+14, y+7, x+25, y);
        g.drawLine(x+26, y-6, x+26, y+6);
        g.drawLine(x+26, y, x+38, y);
    }
}
