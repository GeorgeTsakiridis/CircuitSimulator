package com.tsaky.circuitsimulator.ui.window;

import com.tsaky.circuitsimulator.ChipSimulator;
import com.tsaky.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class SettingsWindow {

    public SettingsWindow(JFrame mainFrame) {

        JPanel applyPanel = new JPanel();
        applyPanel.add(new JButton("Cancel"));
        applyPanel.add(new JButton("Apply"));
        applyPanel.setBorder(BorderFactory.createEtchedBorder());

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(BorderFactory.createEtchedBorder());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        //language
        Map<String, Locale> map = Localization.getAvailableLocales();

        JComboBox<String> cb = new JComboBox<>(map.keySet().toArray(new String[0]));

        settingsPanel.add(createPanelWithComponents(new JLabel(Localization.getString("language")), cb));

        jScrollPane.setViewportView(settingsPanel);

        JDialog frame = new JDialog(mainFrame, Localization.getString("settings"));
        frame.setLayout(new BorderLayout());
        frame.add(jScrollPane, "Center");
        frame.add(applyPanel, "South");

        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.pack();
        frame.setMinimumSize(new Dimension(frame.getSize().width + 20, frame.getSize().height + 20));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(mainFrame);
        frame.setVisible(true);

    }

    private JPanel createPanelWithComponents(JComponent... components){
        JPanel panel = new JPanel();
        for(JComponent component : components)panel.add(component);
        return panel;
    }

}
