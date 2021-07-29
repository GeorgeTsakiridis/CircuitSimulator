package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinGround;
import com.tsaky.circuitsimulator.chip.pin.PinInput;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Chip7SegmentDisplay extends Chip {

    private final int sbd = 39;//segment big dimension
    private final int ssd = 13;//segment small dimension

    private BufferedImage verticalSegment = null, horizontalSegment = null;
    {
        try {
            verticalSegment = ImageIO.read(getClass().getClassLoader().getResource("assets/components/verticalSegment.png"));
            horizontalSegment = ImageIO.read(getClass().getClassLoader().getResource("assets/components/horizontalSegment.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean a = false;
    private boolean b = false;
    private boolean c = false;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;

    public Chip7SegmentDisplay() {
        super("7 Segment Display",
                new InfoPage("Common Cathode 7-Segment Display"),
                new Pin[]{
                        new PinInput("a", 0),
                        new PinInput("b", 1),
                        new PinInput("c", 2),
                        new PinInput("d", 3),
                        new PinInput("e", 4),
                        new PinInput("f", 5),
                        new PinInput("g", 6),
                        new PinGround("-", 7)
        });
        setSizeWithoutPins(80, 121);
    }

    @Override
    public boolean isPowered() {
        return getGroundPin().isGrounded();
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
        }else {
            a = getInputPin(0).isLinkHigh();
            b = getInputPin(1).isLinkHigh();
            c = getInputPin(2).isLinkHigh();
            d = getInputPin(3).isLinkHigh();
            e = getInputPin(4).isLinkHigh();
            f = getInputPin(5).isLinkHigh();
            g = getInputPin(6).isLinkHigh();
        }
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY) {
        int x = getPosX() + offsetX - getWidth() / 2;
        int y = getPosY() + offsetY - getHeight() / 2;
        int sX = x+20;

        g.drawRect(x, y, getWidth(), getHeight());
        int pinSize = PaintUtils.getPinSize(getPinNumber(), getHeight()-20);
        int spacePerPin = getHeight()/getPinNumber() - pinSize;

        int i = 0;
        for(Pin pin : getPins()){
            pin.setBounds(x - offsetX, y + (pinSize+spacePerPin)*i + 2 - offsetY, pinSize, pinSize);
            pin.paintWithPinName(g, x + 2, y + (pinSize+spacePerPin)*i++ +2, pinSize, pin.getName());
        }

        if(verticalSegment != null && horizontalSegment != null){

            if(this.a) g.drawImage(horizontalSegment, sX+11, y+12, null);//a
            if(this.b) g.drawImage(verticalSegment, sX+6+sbd, y+ssd+7, null);//b
            if(this.c) g.drawImage(verticalSegment, sX+6+sbd, y+ssd+sbd+10, null);//c
            if(this.d) g.drawImage(horizontalSegment, sX+11, y+getHeight()-11-ssd, null);//d
            if(this.f) g.drawImage(verticalSegment, sX+3, y+ssd+7, null);//f
            if(this.e) g.drawImage(verticalSegment, sX+3, y+ssd+sbd+10, null);//e
            if(this.g) g.drawImage(horizontalSegment, sX+11, y+getHeight()/2-ssd/2, null);//g


        }

    }
}
