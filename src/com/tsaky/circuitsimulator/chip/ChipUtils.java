package com.tsaky.circuitsimulator.chip;

import com.sun.istack.internal.Nullable;
import com.tsaky.circuitsimulator.Linker;
import com.tsaky.circuitsimulator.chip.pin.Pin;

import java.util.ArrayList;

public class ChipUtils {
    public static void unselectAllPins(ArrayList<Chip> chips){
        for(Chip chip : chips){
            for(Pin pin : chip.getPins()){
                pin.setSelected(false);
            }
        }
    }

    public static Pin getPinBellowMouse(ArrayList<Chip> chips, int x, int y){
        for(Chip chip : chips){
            for(Pin pin : chip.getPins()){
                if(pin.getBounds().contains(x, y))return pin;
            }
        }
        return null;
    }

    public static Chip getChipBellowMouse(ArrayList<Chip> chips, int x, int y){
        for(Chip chip : chips){
            if(chip.getBorder().contains(x, y)){
                return chip;
            }
        }
        return null;
    }

    public static void selectChipIfNotNull(Chip chip){
        if(chip != null)chip.setSelected(true);
    }

    public static void unselectAllChips(ArrayList<Chip> chips){
        for(Chip chip : chips){
            chip.setSelected(false);
        }
    }

    public static boolean chipCollidesWithOtherChip(Chip chip, @Nullable ArrayList<Chip> chips){
        if(chips == null)return false;

        for(Chip c : chips){
            if(c.getBorder().intersects(chip.getBorder())){
                return true;
            }
        }
        return false;
    }

    public static void safelyRemoveChip(ArrayList<Chip> chips, Chip chip, Linker linker) {
        for (Pin pin : chip.getPins()) {
            linker.unlinkPin(pin);
        }

        chips.remove(chip);
    }

}
