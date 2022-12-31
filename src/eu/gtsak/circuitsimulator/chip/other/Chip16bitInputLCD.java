package eu.gtsak.circuitsimulator.chip.other;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;
import eu.gtsak.circuitsimulator.ui.PaintUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Locale;

public class Chip16bitInputLCD extends Chip {

    private int number = 0;

    public Chip16bitInputLCD(){
        super("16-Bit Input", Localization.getString("chip16bit_input_name"),
                new Pin[]{
                        new Pin("Bit 0  ", 0, PinType.INPUT),
                        new Pin("Bit 1  ", 1, PinType.INPUT),
                        new Pin("Bit 2  ", 2, PinType.INPUT),
                        new Pin("Bit 3  ", 3, PinType.INPUT),
                        new Pin("Bit 4  ", 4, PinType.INPUT),
                        new Pin("Bit 5  ", 5, PinType.INPUT),
                        new Pin("Bit 6  ", 6, PinType.INPUT),
                        new Pin("Bit 7  ", 7, PinType.INPUT),
                        new Pin("Bit 8  ", 8, PinType.INPUT),
                        new Pin("Bit 9  ", 9, PinType.INPUT),
                        new Pin("Bit 10", 10, PinType.INPUT),
                        new Pin("Bit 11", 11, PinType.INPUT),
                        new Pin("Bit 12", 12, PinType.INPUT),
                        new Pin("Bit 13", 13, PinType.INPUT),
                        new Pin("Bit 14", 14, PinType.INPUT),
                        new Pin("Bit 15", 15, PinType.INPUT)
                });

        setSize(50, 300);
    }

    @Override
    public String getDescription() {
        return Localization.getString("chip16bit_input_description");
    }

    @Override
    public void calculate() {

        int n = 0;

        for(int i = getPinNumber()-1; i >= 0; i--){
            n = n << 1;
            n += getPin(i).isLinkHigh() ? 1 : 0;
        }

        number = n;
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        g.drawRect(getPosX() + offsetX - getBorder().width/2, getPosY() + offsetY - getBorder().height/2, getBorder().width, getBorder().height);

        for (int i = 0; i < getPinNumber(); i++) {
            getPin(i).setBounds(getPosX() - getWidth()/2 - 23, getPosY()-getHeight()/2 + i*16 + 40, 18, 14);
            getPin(i).paint(g, offsetX, offsetY, true, false);
        }

        String hexString = Integer.toHexString(number).toUpperCase(Locale.ROOT);
        while(hexString.length() < 4){
            hexString = "0".concat(hexString);
        }
        Rectangle stringBounds = PaintUtils.getStringBounds(g, hexString);
        String nameString = realName ? getSaveName() : getDisplayName();
        Rectangle string2Bounds = PaintUtils.getStringBounds(g, nameString);

        g.drawString(hexString, getPosX() + offsetX - stringBounds.width/2, getPosY() + offsetY - getHeight()/2 + stringBounds.height + 10);

        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

        g2d.translate(getPosX() + offsetX + string2Bounds.height + 20, getPosY() + offsetY - string2Bounds.width/2);
        g2d.rotate(Math.PI/2);
        g2d.drawString(nameString, 0, 0);

        g2d.setTransform(old);
    }
}
