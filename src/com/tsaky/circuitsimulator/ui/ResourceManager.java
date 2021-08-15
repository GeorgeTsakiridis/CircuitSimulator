package com.tsaky.circuitsimulator.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ResourceManager {

    private final static HashMap<String, ImageIcon> resources = new HashMap<>();

    public enum EnumSubFolder {
        MOUSE_FUNCTION("buttons/mouse_function/"),
        PROJECT("buttons/project/"),
        SIMULATION("buttons/simulation/"),
        VIEW("buttons/view/"),
        COMPONENT("components/");

        private final String folderName;

        EnumSubFolder(String folderName){
            this.folderName = folderName;
        }

        String getFolderName(){
            return folderName;
        }

    }

    public static void addResource(String name, EnumSubFolder folder, String assetName){
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("assets/" + folder.getFolderName() + assetName))));
            resources.put(name, icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageIcon getResource(String name){
        return resources.get(name);
    }

}
