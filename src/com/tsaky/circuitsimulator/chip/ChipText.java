package com.tsaky.circuitsimulator.chip;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import java.awt.*;

public class ChipText extends Chip{

    String text = "Text";
    String[] splitText = new String[]{};

    boolean flag = true;

    public ChipText() {
        super("Text", new InfoPage("A simple text box"), null);
        setText(text);
    }

    @Override
    public void calculate() {

    }

    public void setText(String text) {
        this.text = text;
        splitText = text.split("\n");
        flag = true;
    }

    public String getText(){
        return text;
    }

    @Override
    public void setExtraData(byte[] bytes) {
        setText(new String(bytes));
    }

    @Override
    public byte[] getExtraDataBytes() {
        return text.getBytes();
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {

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
