package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.chip.c74xx.*;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74257;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74283;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74573;
import com.tsaky.circuitsimulator.chip.c74xxx.Chip74574;
import com.tsaky.circuitsimulator.chip.c74xxxx.Chip744511;
import com.tsaky.circuitsimulator.chip.generic.Chip7SegmentDisplay;
import com.tsaky.circuitsimulator.chip.generic.ChipLED;
import com.tsaky.circuitsimulator.chip.generic.ChipSwitch;

import javax.swing.*;
import java.awt.*;

public class ChipSimulator {

    public ChipSimulator() {
        ChipManager.addChip("Switch", new ChipSwitch());
        ChipManager.addChip("LED Red", new ChipLED("Red", Color.RED));
        ChipManager.addChip("LED Green", new ChipLED("Green", Color.GREEN));
        ChipManager.addChip("LED Yellow", new ChipLED("Yellow", Color.YELLOW));
        ChipManager.addChip("LED Blue", new ChipLED("Blue", Color.BLUE));
        ChipManager.addChip("7-Segment Display", new Chip7SegmentDisplay());

        ChipManager.addChip("7402", new Chip7402()); //Texture OK
        ChipManager.addChip("7404", new Chip7404()); //Texture OK
        ChipManager.addChip("7408", new Chip7408()); //Texture OK
        ChipManager.addChip("7432", new Chip7432()); //Texture OK
        ChipManager.addChip("7486", new Chip7486()); //Texture OK
        ChipManager.addChip("74257", new Chip74257()); //Texture OK
        ChipManager.addChip("74283", new Chip74283()); //Texture OK
        ChipManager.addChip("74573", new Chip74573()); //Texture OK
        ChipManager.addChip("74574", new Chip74574()); //Texture OK
        ChipManager.addChip("744511", new Chip744511()); //Texture OK

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
