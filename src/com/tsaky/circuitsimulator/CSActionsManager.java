package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.ui.LineViewMode;
import com.tsaky.circuitsimulator.ui.PinViewMode;
import com.tsaky.circuitsimulator.ui.ResourceManager;
import com.tsaky.circuitsimulator.ui.window.ViewportPanel;
import com.tsaky.circuitsimulator.ui.window.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSActionsManager {

    private static final HashMap<String, CSAction> actions = new HashMap<>();

    public static void addAction(String name, String iconAssetName, KeyStroke keystroke, int mnemonic, Action action, JMenu menu){
        addAction(name, iconAssetName, keystroke, mnemonic, action, menu, false);
    }

    public static void addAction(String name, String iconAssetName, KeyStroke keystroke, int mnemonic, Action action, JMenu menu, boolean addSeperatorBeforeEntry){
        actions.put(name, new CSAction(name, iconAssetName, keystroke, mnemonic, action, menu, addSeperatorBeforeEntry));
    }

    public static CSAction getAction(String name){
        return actions.get(name);
    }

    public static void bindAllTo(JPanel panel){
        for(CSAction action : actions.values()){
            action.bindTo(panel);
        }
    }

}
