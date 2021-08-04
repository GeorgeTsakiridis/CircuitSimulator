package com.tsaky.circuitsimulator.chip;

import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import java.awt.*;

public class ChipText extends Chip{

    String text = "Text";
    String[] splitText = new String[]{};

    boolean flag = true;

    public ChipText() {
        super("text", "Text", new Pin[]{});
        setDisplayName(text);
    }

    @Override
    public String getDescription() {
        return "A simple text box";
    }

    @Override
    public void calculate() {

    }

    @Override
    public void setDisplayName(String text) {
        this.text = text;
        splitText = text.split("\n");
        flag = true;
    }

    @Override
    public String getDisplayName() {
        return text;
    }

    @Override
    public void setExtraData(byte[] bytes) {
        setDisplayName(new String(bytes));
    }

    @Override
    public byte[] getExtraDataBytes() {
        return text.getBytes();
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {

        Rectangle bounds = PaintUtils.getMultilineStringBounds(g, splitText);
        setSizeWithoutPins(bounds.width, bounds.height);

        int x = getPosX() + offsetX - getWidth()/2;
        int y = getPosY() + offsetY + g.getFont().getSize() - getHeight()/2;

        if(flag){
            flag = false;
            setPosition(getPosX(), getPosY());
        }

        for(int i = 0; i < splitText.length; i++){
            g.drawString(splitText[i], x, y + i*g.getFont().getSize());
        }

    }
}
