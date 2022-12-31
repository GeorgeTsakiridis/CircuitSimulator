package eu.gtsak.circuitsimulator.chip;

import eu.gtsak.circuitsimulator.logic.Linker;
import eu.gtsak.circuitsimulator.chip.pin.Pin;

import java.util.ArrayList;

public class ChipUtils {
    public static void unselectAllPins(ArrayList<Chip> chips){
        for(Chip chip : chips){
            if(chip.getPinNumber() == 0)continue;
            for(Pin pin : chip.getPins()){
                pin.setSelected(false);
            }
        }
    }

    public static Pin getPinBellowMouse(ArrayList<Chip> chips, int x, int y){
        for(Chip chip : chips){
            if(chip.getPinNumber() == 0)continue;
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

    public static boolean chipCollidesWithOtherChip(Chip chip, ArrayList<Chip> chips){
        if(chips == null)return false;

        for(Chip c : chips){
            if(c.getBorder().intersects(chip.getBorder())){
                return true;
            }
        }
        return false;
    }

    public static void safelyRemoveChip(ArrayList<Chip> chips, Chip chip) {
        chip.onRemove();
        if(chip.getPinNumber() > 0) {
            for (Pin pin : chip.getPins()) {
                Linker.unlinkPin(pin);
            }
        }
        chips.remove(chip);
    }

    public static int[] getChipIndexAndPinIndex(ArrayList<Chip> chips, Pin pin){
        int[] indexes = new int[]{-1, -1};

        for(int j = 0; j < chips.size(); j++){
            Pin[] pins = chips.get(j).getPins();

            for(int k = 0; k < pins.length; k++){
                if(pins[k] == pin){
                    indexes[0] = j;
                    indexes[1] = k;
                    return indexes;
                }
            }
        }
        return indexes;
    }

}
