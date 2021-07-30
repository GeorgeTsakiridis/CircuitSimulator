package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.chip.ChipText;
import com.tsaky.circuitsimulator.chip.c74xx.*;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74257;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74283;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74573;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74574;
import com.tsaky.circuitsimulator.chip.c74xxxx.Chip744511;
import com.tsaky.circuitsimulator.chip.generic.*;

import javax.swing.*;
import java.awt.*;

public class ChipSimulator {

    public static final int PROGRAM_VERSION = 300721;
    public static final long MAGIC_NUMBER = 1073147341387874804L;

    public ChipSimulator() {
        ChipManager.addChip(new ChipNode());
        ChipManager.addChip(new ChipGround());
        ChipManager.addChip(new ChipPower());
        ChipManager.addChip(new ChipPowerSwitch());
        ChipManager.addChip(new ChipText());
        ChipManager.addChip(new ChipLED("Red", Color.RED));
        ChipManager.addChip(new ChipLED("Green", Color.GREEN));
        ChipManager.addChip(new ChipLED("Yellow", Color.YELLOW));
        ChipManager.addChip(new ChipLED("Blue", Color.BLUE));
        ChipManager.addChip(new Chip7SegmentDisplay());

        ChipManager.addChip(new Chip7402());
        ChipManager.addChip(new Chip7404());
        ChipManager.addChip(new Chip7408());
        ChipManager.addChip(new Chip7432());
        ChipManager.addChip(new Chip7486());
        ChipManager.addChip(new Chip74257());
        ChipManager.addChip(new Chip74283());
        ChipManager.addChip(new Chip74573());
        ChipManager.addChip(new Chip74574());
        ChipManager.addChip(new Chip744511());

        new Handler();

    }

    public static void main(String[] args) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            System.err.println("Couldn't change system look & feel.");
        }

        new ChipSimulator();
    }

}
