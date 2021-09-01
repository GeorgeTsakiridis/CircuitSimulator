package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.Localization;

import java.io.*;

public class Chip74574 extends Chip {

    private boolean s0 = false;
    private boolean s1 = false;
    private boolean s2 = false;
    private boolean s3 = false;
    private boolean s4 = false;
    private boolean s5 = false;
    private boolean s6 = false;
    private boolean s7 = false;

    private boolean lastCLK = false;

    public Chip74574() {
        super("74574", Localization.getString("chip74574_name"),
                new Pin[]{
                        new Pin("-OE", 0, PinType.INPUT),
                        new Pin("D1", 1, PinType.INPUT),
                        new Pin("D2", 2, PinType.INPUT),
                        new Pin("D3", 3, PinType.INPUT),
                        new Pin("D4", 4, PinType.INPUT),
                        new Pin("D5", 5, PinType.INPUT),
                        new Pin("D6", 6, PinType.INPUT),
                        new Pin("D7", 7, PinType.INPUT),
                        new Pin("D8", 8, PinType.INPUT),
                        new Pin("GND", 9, PinType.GROUND),
                        new Pin("CLK", 10, PinType.INPUT),
                        new Pin("8Q", 11, PinType.OUTPUT),
                        new Pin("7Q", 12, PinType.OUTPUT),
                        new Pin("6Q", 13, PinType.OUTPUT),
                        new Pin("5Q", 14, PinType.OUTPUT),
                        new Pin("4Q", 15, PinType.OUTPUT),
                        new Pin("3Q", 16, PinType.OUTPUT),
                        new Pin("2Q", 17, PinType.OUTPUT),
                        new Pin("1Q", 18, PinType.OUTPUT),
                        new Pin("VCC", 19, PinType.POWER)
                });
        setSize(40, 220);
    }

    @Override
    public byte[] getExtraDataBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeBoolean(s0);
            dataOutputStream.writeBoolean(s1);
            dataOutputStream.writeBoolean(s2);
            dataOutputStream.writeBoolean(s3);
            dataOutputStream.writeBoolean(s4);
            dataOutputStream.writeBoolean(s5);
            dataOutputStream.writeBoolean(s6);
            dataOutputStream.writeBoolean(s7);
            dataOutputStream.writeBoolean(lastCLK);
        }catch (IOException e){
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void setExtraData(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        try {
            s0 = dataInputStream.readBoolean();
            s1 = dataInputStream.readBoolean();
            s2 = dataInputStream.readBoolean();
            s3 = dataInputStream.readBoolean();
            s4 = dataInputStream.readBoolean();
            s5 = dataInputStream.readBoolean();
            s6 = dataInputStream.readBoolean();
            s7 = dataInputStream.readBoolean();
            lastCLK = dataInputStream.readBoolean();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return Localization.getString("chip74574_description");
    }

    @Override
    public void calculate() {
        if (!isPowered()) {
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        } else {
            boolean OE = getPin(0).isLinkHigh();
            boolean CLK = getPin(10).isLinkHigh();
            if (!lastCLK && CLK) {
                s0 = getPin(1).isLinkHigh();
                s1 = getPin(2).isLinkHigh();
                s2 = getPin(3).isLinkHigh();
                s3 = getPin(4).isLinkHigh();
                s4 = getPin(5).isLinkHigh();
                s5 = getPin(6).isLinkHigh();
                s6 = getPin(7).isLinkHigh();
                s7 = getPin(8).isLinkHigh();
            }
            lastCLK = CLK;
            if (!OE) {
                turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
                getPin(11).setHigh(s7);
                getPin(12).setHigh(s6);
                getPin(13).setHigh(s5);
                getPin(14).setHigh(s4);
                getPin(15).setHigh(s3);
                getPin(16).setHigh(s2);
                getPin(17).setHigh(s1);
                getPin(18).setHigh(s0);
            } else {
                turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
            }
        }
    }
}
