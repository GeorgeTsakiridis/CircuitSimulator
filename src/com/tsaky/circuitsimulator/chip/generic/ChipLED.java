package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinInput;

import java.awt.*;

public class ChipLED extends Chip {

    private Color color;

    public ChipLED(){
        this("None", Color.BLACK);
    }

    public ChipLED(String ledColor, Color color) {
        super(ledColor + "LED",
                new InfoPage("Colored LED"),
                new Pin[]{new PinInput("IN", 0)});
        this.color = color;
        setSizeWithoutPins(15, 15);
    }

    private void setColor(Color color){
        this.color = new Color(color.getRGB());

    }

    @Override
    public byte[] getExtraDataBytes() {
        byte b;

        if (color == Color.RED) {
            return new byte[]{0};
        } else if (color == Color.GREEN) {
            return new byte[]{1};
        } else if (color == Color.YELLOW) {
            return new byte[]{2};
        } else if (color == Color.BLUE) {
            return new byte[]{3};
        }
        return new byte[]{-1};

    }

    @Override
    public void setExtraData(byte[] bytes) {

        if(bytes[0] == 0){
            color = Color.RED;
        }else if(bytes[0] == 1){
            color = Color.GREEN;
        }else if(bytes[0] == 2){
            color = Color.YELLOW;
        }else if(bytes[0] == 3){
            color = Color.BLUE;
        }

    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public Chip createNewInstance() {
        ChipLED led = (ChipLED) super.createNewInstance();
        led.setColor(color);
        return led;
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() + offsetX - getWidth()/2;
        int y = getPosY() + offsetY - getHeight()/2;

        getPin(0).paintWithPinName(g, x, y, getWidth(), "");

        if(((PinInput)getPin(0)).isLinkHigh()) {
            Color c = g.getColor();
            g.setColor(color);
            g.fillOval(x, y, getWidth(), getHeight());
            g.setColor(c);
        }else{
            g.drawOval(x, y, getWidth(), getHeight());
        }
    }
}
