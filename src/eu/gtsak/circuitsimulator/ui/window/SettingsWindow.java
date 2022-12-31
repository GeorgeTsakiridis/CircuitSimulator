package eu.gtsak.circuitsimulator.ui.window;

import eu.gtsak.circuitsimulator.settings.Settings;
import eu.gtsak.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class SettingsWindow {

    public SettingsWindow(JFrame mainFrame) {

        //Panel for OK and Cancel buttons
        JButton okButton = new JButton(Localization.getString("okay"));
        JButton cancelButton = new JButton(Localization.getString("cancel"));

        JPanel applyPanel = new JPanel();
        applyPanel.add(okButton);
        applyPanel.add(cancelButton);
        applyPanel.setBorder(BorderFactory.createEtchedBorder());

        //Panel that contains settings
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(BorderFactory.createEtchedBorder());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        //Add settings to panel
        //language
        Map<String, Locale> localesMap = Localization.getAvailableLocales();
        int index = 0;
        for(Locale locale : localesMap.values()){
            if(locale.equals(Localization.getLocale()))break;
            index++;
        }
        JComboBox<String> cb = new JComboBox<>(localesMap.keySet().toArray(new String[0]));
        cb.setSelectedIndex(index);

        settingsPanel.add(createPanelWithComponents(new JLabel(Localization.getString("language")), cb, getNeedsRestartLabel()));

        //Create actual window
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

        //Add listeners for OK and Cancel buttons
        cancelButton.addActionListener(e -> frame.dispose());

        okButton.addActionListener(e -> {
            try {
                Localization.setLocale(localesMap.get((String)cb.getSelectedItem()));
                Settings.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private JLabel getNeedsRestartLabel(){
        return new JLabel(Localization.getString("needs_restart"));
    }

    private JPanel createPanelWithComponents(JComponent... components){
        JPanel panel = new JPanel();
        for(JComponent component : components)panel.add(component);
        return panel;
    }

}
