package eu.gtsak.circuitsimulator.chip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ChipManager {

    private static final Map<String, Chip> chips = new LinkedHashMap<>();

    public static void addChip(Chip chip){
        chips.put(chip.getSaveName(), chip);
    }

    public static Chip getNewChipInstance(String chipName){//Accepts either the Save name or the Catalog name
        Chip chip = chips.get(chipName);
        if(chip == null){
            for(Map.Entry<String, Chip> e : chips.entrySet()){
                if(e.getValue().getDisplayName().equals(chipName))chip = e.getValue();
            }
        }

        return Objects.requireNonNull(chip).createNewInstance();
    }

    public static String[] getAllChipNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Map.Entry<String, Chip> e : chips.entrySet()){
            names.add(e.getValue().getDisplayName());
        }

        return  names.toArray(new String[0]);
    }

}
