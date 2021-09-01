package com.tsaky.circuitsimulator.chip.other;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.Localization;
import com.tsaky.circuitsimulator.ui.PaintUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.Locale;

public class Chip16bitOutputLCD extends Chip {

    private int outputNumber = 0;

    public Chip16bitOutputLCD(){
        super("16-Bit Output", Localization.getString("chip16bit_output_name"),
                new Pin[]{
                        new Pin("Bit 0  ", 0, PinType.OUTPUT),
                        new Pin("Bit 1  ", 1, PinType.OUTPUT),
                        new Pin("Bit 2  ", 2, PinType.OUTPUT),
                        new Pin("Bit 3  ", 3, PinType.OUTPUT),
                        new Pin("Bit 4  ", 4, PinType.OUTPUT),
                        new Pin("Bit 5  ", 5, PinType.OUTPUT),
                        new Pin("Bit 6  ", 6, PinType.OUTPUT),
                        new Pin("Bit 7  ", 7, PinType.OUTPUT),
                        new Pin("Bit 8  ", 8, PinType.OUTPUT),
                        new Pin("Bit 9  ", 9, PinType.OUTPUT),
                        new Pin("Bit 10", 10, PinType.OUTPUT),
                        new Pin("Bit 11", 11, PinType.OUTPUT),
                        new Pin("Bit 12", 12, PinType.OUTPUT),
                        new Pin("Bit 13", 13, PinType.OUTPUT),
                        new Pin("Bit 14", 14, PinType.OUTPUT),
                        new Pin("Bit 15", 15, PinType.OUTPUT)
                });

        setSize(50, 300);
    }

    @Override
    public void toggle() {
        String numberInput = JOptionPane.showInputDialog(Localization.getString("chip16bit_output_insert_4_digit_hex_number"));
        if (numberInput.matches("[0-9a-fA-F]+")) {
            if (numberInput.length() <= 4) {
                outputNumber = Integer.valueOf(numberInput, 16);
            } else {
                JOptionPane.showConfirmDialog(null, Localization.getString("chip16bit_output_exceeded_digits_message"), Localization.getString("chip16bit_output_exceeded_digits_title"), JOptionPane.DEFAULT_OPTION);
            }
        } else {
            JOptionPane.showConfirmDialog(null, Localization.getString("chip16bit_output_invalid_number_message"), Localization.getString("chip16bit_output_invalid_number_title"), JOptionPane.DEFAULT_OPTION);
        }
    }

    @Override
    public String getDescription() {
        return Localization.getString("chip16bit_output_descriptor");
    }

    @Override
    public byte[] getExtraDataBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try{
            dataOutputStream.writeInt(outputNumber);
        }catch (IOException e){
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void setExtraData(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try{
            outputNumber = dataInputStream.readInt();
        }catch (IOException e){
            e.printStackTrace();
        }
        calculate();
    }

    @Override
    public void calculate() {

        int value = outputNumber+1;

        for(int i = getPinNumber()-1; i >= 0; i--){
            int bitValue = (int)Math.pow(2, i);
            if(value > bitValue){
                getPin(i).setHigh(true);
                value -= bitValue;
            }else{
                getPin(i).setHigh(false);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        g.drawRect(getPosX() + offsetX - getBorder().width/2, getPosY() + offsetY - getBorder().height/2, getBorder().width, getBorder().height);

        for (int i = 0; i < getPinNumber(); i++) {
            getPin(i).setBounds(getPosX() + getWidth()/2 + 5, getPosY()-getHeight()/2 + i*16 + 40, 18, 14);
            getPin(i).paint(g, offsetX, offsetY, true, true);
        }

        String hexString = Integer.toHexString(outputNumber).toUpperCase(Locale.ROOT);
        while(hexString.length() < 4){
            hexString = "0".concat(hexString);
        }
        Rectangle stringBounds = PaintUtils.getStringBounds(g, hexString);
        String nameString = realName ? getSaveName() : getDisplayName();
        Rectangle string2Bounds = PaintUtils.getStringBounds(g, nameString);



        g.drawString(hexString, getPosX() + offsetX - stringBounds.width/2 + 5, getPosY() + offsetY - getHeight()/2 + stringBounds.height + 10);

        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

        g2d.translate(getPosX() + offsetX - 35, getPosY() + offsetY + string2Bounds.width/2);
        g2d.rotate(-Math.PI/2);
        g2d.drawString(nameString, 0, 0);

        g2d.setTransform(old);
    }
}
