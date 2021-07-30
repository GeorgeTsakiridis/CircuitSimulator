package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinInput;

import java.awt.*;

public class ChipLED extends Chip {

    private Color colorActive;
    private Color colorInactive;

    public ChipLED(){
        this("Red", Color.RED);
    }

    public ChipLED(String ledColor, Color color) {
        super("LED " + ledColor,
                new InfoPage("Colored LED"),
                new Pin[]{new PinInput("IN", 0)});
        setColor(color);
        setSizeWithoutPins(15, 15);
    }

    private void setColor(Color color){
        this.colorActive = new Color(color.getRGB());

        int r = colorActive.getRed();
        int g = colorActive.getGreen();
        int b = colorActive.getBlue();

        r = Math.min(255, r + 150);
        g = Math.min(255, g + 150);
        b = Math.min(255, b + 150);

        this.colorInactive = new Color(r, g, b);
    }

    @Override
    public byte[] getExtraDataBytes() {

        if (colorActive.equals(Color.RED)) {
            return new byte[]{0};
        } else if (colorActive.equals(Color.GREEN)) {
            return new byte[]{1};
        } else if (colorActive.equals(Color.YELLOW)) {
            return new byte[]{2};
        } else if (colorActive.equals(Color.BLUE)) {
            return new byte[]{3};
        }

        return new byte[]{-1};

    }

    @Override
    public void setExtraData(byte[] bytes) {
        if(bytes[0] == 0){
            setColor(Color.RED);
        }else if(bytes[0] == 1){
            setColor(Color.GREEN);
        }else if(bytes[0] == 2){
            setColor(Color.YELLOW);
        }else if(bytes[0] == 3){
            setColor(Color.BLUE);
        }
    }

    @Override
    public void calculateOutputs() {

    }

    @Override
    public Chip createNewInstance() {
        ChipLED led = (ChipLED) super.createNewInstance();
        led.setColor(colorActive);
        return led;
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() - getWidth()/2;
        int y = getPosY() - getHeight()/2;

        getPin(0).setBounds(x, y, getWidth(), getHeight());
        getPin(0).paintWithPinName(g,offsetX, offsetY, "");

        Color c = g.getColor();

        if(((PinInput)getPin(0)).isLinkHigh()) {
            g.setColor(colorActive);
        }else{
            g.setColor(colorInactive);
        }

        g.fillOval(x + offsetX, y + offsetY, getWidth(), getHeight());

        g.setColor(c);
    }
}
