package com.tsaky.circuitsimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InfoPage {
    private String description;
    private BufferedImage schmematic;

    public InfoPage(String description){
        this(description, null);
    }

    public InfoPage(String description, String schmematicName){
        this.description = description;
        if(schmematicName != null) {
            try {
                this.schmematic = ImageIO.read(getClass().getClassLoader().getResource("assets/schematics/" + schmematicName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.schmematic = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getSchmematic(){
        return schmematic;
    }

}
