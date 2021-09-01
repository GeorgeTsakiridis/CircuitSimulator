package com.tsaky.circuitsimulator.logic;

import com.tsaky.circuitsimulator.ui.ResourceManager;

import javax.swing.*;

public class CSAction {

    private final String name;
    private final String iconAssetName;
    private final KeyStroke keystroke;
    private final int mnemonic;
    private final Action action;
    private final JMenu menu;

    public CSAction(String name, String iconAssetName, KeyStroke keystroke, int mnemonic, Action action, JMenu menu, boolean addSeperatorBeforeEntry) {
        this.name = name;
        this.iconAssetName = iconAssetName;
        this.keystroke = keystroke;
        this.mnemonic = mnemonic;
        this.action = action;
        this.menu = menu;
        addToMenuBar(addSeperatorBeforeEntry);
    }

    public String getName() {
        return name;
    }

    public String getIconAssetName() {
        return iconAssetName;
    }

    public KeyStroke getKeystroke() {
        return keystroke;
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public Action getAction() {
        return action;
    }

    public void bindTo(JPanel panel) {
        if (getKeystroke() != null) {
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(getKeystroke(), getName());
            panel.getActionMap().put(getName(), getAction());
        }
    }

    public void addToMenuBar(boolean addSeperatorBeforeEntry){
        if(menu != null) {

            if(addSeperatorBeforeEntry){
                menu.addSeparator();
            }

            JMenuItem jMenuItem = new JMenuItem(getName(), getMnemonic());
            jMenuItem.addActionListener(action);

            if (getIconAssetName() != null) {
                jMenuItem.setIcon(ResourceManager.getResource(iconAssetName));
            }
            if (getKeystroke() != null) {
                jMenuItem.setAccelerator(getKeystroke());
            }

            menu.add(jMenuItem);
        }
    }

}
