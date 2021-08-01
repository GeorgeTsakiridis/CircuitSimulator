package com.tsaky.circuitsimulator.chip.generic;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Chip7SegmentDisplay extends Chip {

    private BufferedImage verticalSegment = null, horizontalSegment = null;
    {
        try {
            verticalSegment = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/components/verticalSegment.png")));
            horizontalSegment = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/components/horizontalSegment.png")));
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
        super("seven_segment_display", "7 Segment Display",
                new InfoPage("Common Cathode 7-Segment Display"),
                new Pin[]{
                        new Pin("a", 0, PinType.INPUT),
                        new Pin("b", 1, PinType.INPUT),
                        new Pin("c", 2, PinType.INPUT),
                        new Pin("d", 3, PinType.INPUT),
                        new Pin("e", 4, PinType.INPUT),
                        new Pin("f", 5, PinType.INPUT),
                        new Pin("g", 6, PinType.INPUT),
                        new Pin("-", 7, PinType.GROUND)
        });
        setSizeWithoutPins(80, 121);
    }

    @Override
    public boolean isPowered() {
        return getGroundPin().isGrounded();
    }

    @Override
    public void calculate() {
        if(!isPowered()){
            turnAllOutputsOff();
        }else {
            a = getPin(0).isLinkHigh();
            b = getPin(1).isLinkHigh();
            c = getPin(2).isLinkHigh();
            d = getPin(3).isLinkHigh();
            e = getPin(4).isLinkHigh();
            f = getPin(5).isLinkHigh();
            g = getPin(6).isLinkHigh();
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
            pin.setBounds(x - offsetX, y + (pinSize+spacePerPin)*i++ + 2 - offsetY, pinSize, pinSize);
            pin.paint(g, offsetX, offsetY, pin.getName());
        }

        if(verticalSegment != null && horizontalSegment != null){

            if(this.a) g.drawImage(horizontalSegment, sX+11, y+12, null);//a
            //segment small dimension
            int ssd = 13;
            //segment big dimension
            int sbd = 39;
            if(this.b) g.drawImage(verticalSegment, sX+6+ sbd, y+ ssd +7, null);//b
            if(this.c) g.drawImage(verticalSegment, sX+6+ sbd, y+ ssd + sbd +10, null);//c
            if(this.d) g.drawImage(horizontalSegment, sX+11, y+getHeight()-11- ssd, null);//d
            if(this.f) g.drawImage(verticalSegment, sX+3, y+ ssd +7, null);//f
            if(this.e) g.drawImage(verticalSegment, sX+3, y+ ssd + sbd +10, null);//e
            if(this.g) g.drawImage(horizontalSegment, sX+11, y+getHeight()/2- ssd /2, null);//g

        }
    }
}
