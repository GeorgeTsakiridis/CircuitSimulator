package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.chip.Chip;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ComponentInfoPanel extends JPanel {

    private Chip chip = null;

    public ComponentInfoPanel(){
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    repaint();
            }
        }, 0, 16);

    }

    public void setChip(Chip chip){
        this.chip = chip;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color oldColor = g.getColor();

        if (chip != null) {
            chip.paintComponent(g, getWidth()/2-chip.getPosX(), getHeight()/2-chip.getPosY(), true, true);
        }

        g.setColor(oldColor);

    }
}
