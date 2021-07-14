package com.tsaky.circuitsimulator.chip;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChipManager {

    private static final Map<String, Chip> chips = new LinkedHashMap<>();

    public static void addChip(Chip chip){
        chips.put(chip.getChipName(), chip);
    }

    public static Chip getNewChipInstance(String name){
        return chips.get(name).createNewInstance();
    }

    public static String[] getAllChipNames(){
        return  chips.keySet().toArray(new String[0]);
    }

}
