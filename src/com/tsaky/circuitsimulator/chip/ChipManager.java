package com.tsaky.circuitsimulator.chip;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChipManager {

    private static Map<String, Chip> chips = new LinkedHashMap<>();

    public static void addChip(String name, Chip chip){
        chips.put(name, chip);
    }

    public static Chip getNewChipInstance(String name){
        return chips.get(name).createNewInstance();
    }

    public static String[] getAllChipNames(){
        return  chips.keySet().toArray(new String[0]);
    }

}
