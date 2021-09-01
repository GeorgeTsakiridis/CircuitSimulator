package com.tsaky.circuitsimulator.logic;

import javax.swing.*;
import java.util.HashMap;

public class CSActionsManager {

    private static final HashMap<String, CSAction> actions = new HashMap<>();

    public static void addAction(String name, String iconAssetName, KeyStroke keystroke, int mnemonic, Action action, JMenu menu){
        addAction(name, iconAssetName, keystroke, mnemonic, action, menu, false);
    }

    public static void addAction(String name, String iconAssetName, KeyStroke keystroke, int mnemonic, Action action, JMenu menu, boolean addSeperatorBeforeEntry){
        actions.put(name, new CSAction(name, iconAssetName, keystroke, mnemonic, action, menu, addSeperatorBeforeEntry));
    }

    public static void bindAllTo(JPanel panel){
        for(CSAction action : actions.values()){
            action.bindTo(panel);
        }
    }

}
