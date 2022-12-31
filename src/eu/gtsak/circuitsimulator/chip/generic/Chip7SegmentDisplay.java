package eu.gtsak.circuitsimulator.chip.generic;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;
import eu.gtsak.circuitsimulator.ui.PaintUtils;
import eu.gtsak.circuitsimulator.ui.ResourceManager;

import java.awt.*;

public class Chip7SegmentDisplay extends Chip {

    private final Image verticalSegment;
    private final Image horizontalSegment;

    private boolean a = false;
    private boolean b = false;
    private boolean c = false;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;

    public Chip7SegmentDisplay() {
        super("Seven Segment Display", Localization.getString("seven_segment_display_name"),
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

        verticalSegment = ResourceManager.getResource("cmp_vertical_segment").getImage();
        horizontalSegment = ResourceManager.getResource("cmp_horizontal_segment").getImage();
    }

    @Override
    public String getDescription() {
        return Localization.getString("seven_segment_display_description");
    }

    @Override
    public boolean isPowered() {
        return getGroundPin().isGrounded();
    }

    @Override
    public void calculate() {
        if (!isPowered()) {
            turnAllOutputsOff();
        } else {
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
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        int x = getPosX() + offsetX - getWidth() / 2;
        int y = getPosY() + offsetY - getHeight() / 2;
        int sX = x + 20;

        g.drawRect(x, y, getWidth(), getHeight());
        int pinSize = PaintUtils.getPinSize(getPinNumber(), getHeight() - 20);
        int spacePerPin = getHeight() / getPinNumber() - pinSize;

        int i = 0;
        for (Pin pin : getPins()) {
            pin.setBounds(x - offsetX, y + (pinSize + spacePerPin) * i++ + 2 - offsetY, pinSize, pinSize);
            pin.paint(g, offsetX, offsetY, pin.getName(), pinDescription, false);
        }


        //segment small dimension
        int ssd = 13;
        //segment big dimension
        int sbd = 39;

        if (this.a) g.drawImage(horizontalSegment, sX + 11, y + 12, null);//a
        if (this.b) g.drawImage(verticalSegment, sX + 6 + sbd, y + ssd + 7, null);//b
        if (this.c) g.drawImage(verticalSegment, sX + 6 + sbd, y + ssd + sbd + 10, null);//c
        if (this.d) g.drawImage(horizontalSegment, sX + 11, y + getHeight() - 11 - ssd, null);//d
        if (this.f) g.drawImage(verticalSegment, sX + 3, y + ssd + 7, null);//f
        if (this.e) g.drawImage(verticalSegment, sX + 3, y + ssd + sbd + 10, null);//e
        if (this.g) g.drawImage(horizontalSegment, sX + 11, y + getHeight() / 2 - ssd / 2, null);//g

    }
}
