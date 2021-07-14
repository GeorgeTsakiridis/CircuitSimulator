package com.tsaky.circuitsimulator.chip;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChipManager {

    private static final Map<String, Chip> chips = new LinkedHashMap<>();

    public static void addChip(String name, Chip chip){
        chips.put(name, chip);
    }

    public static Chip getNewChipInstance(String name){
        System.out.println("REQUESTED " + name);
        return chips.get(name).createNewInstance();
    }

    public static String[] getAllChipNames(){
        return  chips.keySet().toArray(new String[0]);
    }

}
