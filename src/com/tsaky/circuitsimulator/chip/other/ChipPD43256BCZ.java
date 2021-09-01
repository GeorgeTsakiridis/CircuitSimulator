package com.tsaky.circuitsimulator.chip.other;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.Localization;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ChipPD43256BCZ extends Chip {

    private final Pin[] ioPins;
    private final Pin[] addressPins;

    HashMap<Integer, Integer> ramData = new HashMap<>();

    public ChipPD43256BCZ() {
        super("PD43256BCZ", Localization.getString("chipPD43256BCZ_name"),
                new Pin[]{
                        new Pin("A14", 0, PinType.INPUT),
                        new Pin("A12", 1, PinType.INPUT),
                        new Pin("A7", 2, PinType.INPUT),
                        new Pin("A6", 3, PinType.INPUT),
                        new Pin("A5", 4, PinType.INPUT),
                        new Pin("A4", 5, PinType.INPUT),
                        new Pin("A3", 6, PinType.INPUT),
                        new Pin("A2", 7, PinType.INPUT),
                        new Pin("A1", 8, PinType.INPUT),
                        new Pin("A0", 9, PinType.INPUT),
                        new Pin("I/O1", 10, PinType.HIGH_Z),
                        new Pin("I/O2", 11, PinType.HIGH_Z),
                        new Pin("I/O3", 12, PinType.HIGH_Z),
                        new Pin("GND", 13, PinType.GROUND),
                        new Pin("I/O4", 14, PinType.HIGH_Z),
                        new Pin("I/O5", 15, PinType.HIGH_Z),
                        new Pin("I/O6", 16, PinType.HIGH_Z),
                        new Pin("I/O7", 17, PinType.HIGH_Z),
                        new Pin("I/O8", 18, PinType.HIGH_Z),
                        new Pin("-CS", 19, PinType.INPUT),
                        new Pin("A10", 20, PinType.INPUT),
                        new Pin("-OE", 21, PinType.INPUT),
                        new Pin("A11", 22, PinType.INPUT),
                        new Pin("A9", 23, PinType.INPUT),
                        new Pin("A8", 24, PinType.INPUT),
                        new Pin("A13", 25, PinType.INPUT),
                        new Pin("-WE", 26, PinType.INPUT),
                        new Pin("VCC", 27, PinType.POWER)
                });

        ioPins = new Pin[]{getPin(10), getPin(11), getPin(12), getPin(14), getPin(15), getPin(16), getPin(17), getPin(18)};
        addressPins = new Pin[]{getPin(9), getPin(8), getPin(7), getPin(6), getPin(5), getPin(4), getPin(3),
                getPin(2), getPin(24), getPin(23), getPin(20), getPin(22), getPin(1), getPin(25), getPin(0)};

        setSize(70, 350);
    }

    @Override
    public String getDescription() {
        return Localization.getString("chipPD43256BCZ_description");
    }

    @Override
    public byte[] getExtraDataBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(ramData.size());
            for(Map.Entry<Integer, Integer> entry : ramData.entrySet()) {
                dataOutputStream.writeInt(entry.getKey());
                dataOutputStream.writeInt(entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void setExtraData(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            int entries = dataInputStream.readInt();
            for(int i = 0; i < entries; i++){
                ramData.put(dataInputStream.readInt(), dataInputStream.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getRamValue(int address){
        if(ramData.containsKey(address))return ramData.get(address);

        return 0;
    }

    @Override
    public void calculate() {
        boolean chipSelect = !getPin(19).isLinkHigh();
        boolean outputEnable = !getPin(21).isLinkHigh();
        boolean writeEnable = !getPin(26).isLinkHigh();

        if(!isPowered()){
            turnAllPinTypesTo(ioPins, PinType.HIGH_Z);
            ramData.clear();
        }else{

            int address = 0;
            for(int i = addressPins.length-1; i >= 0; i--){
                address = address << 1;
                address += addressPins[i].isLinkHigh() ? 1 : 0;
            }

            if(chipSelect){
                if(outputEnable && !writeEnable){//Output-Read
                    turnAllPinTypesTo(ioPins, PinType.OUTPUT);

                    int value = getRamValue(address);

                    for(int i = ioPins.length-1; i >= 0; i--){
                        int bitValue = (int)Math.pow(2, i);
                        if(value >= bitValue){
                            ioPins[i].setHigh(true);
                            value -= bitValue;
                        }else{
                            ioPins[i].setHigh(false);
                        }
                    }

                }else if(!outputEnable && writeEnable){//Input-Write
                    turnAllPinTypesTo(ioPins, PinType.INPUT);

                    int value = 0;
                    for (int i = ioPins.length-1; i >= 0 ; i--) {
                        value = value << 1;
                        value += ioPins[i].isLinkHigh() ? 1 : 0;
                    }

                    ramData.put(address, value);

                }else{
                    turnAllPinTypesTo(ioPins, PinType.HIGH_Z);
                }
            }else{
                turnAllPinTypesTo(ioPins, PinType.HIGH_Z);
            }
        }
    }

}
