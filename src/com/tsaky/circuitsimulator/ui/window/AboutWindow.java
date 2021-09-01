package com.tsaky.circuitsimulator.ui.window;

import com.tsaky.circuitsimulator.ChipSimulator;
import com.tsaky.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.awt.*;

public class AboutWindow {

    public AboutWindow(JFrame mainFrame){
        Dimension size = new Dimension(460, 260);

        JDialog frame = new JDialog(mainFrame, Localization.getString("about"));
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel();

        JLabel programName = getCenteredLabel("Circuit Simulator");
        programName.setFont(programName.getFont().deriveFont(50f));

        JLabel programAuthor = new JLabel("by George Tsakiridis");

        frame.setSize(size);
        frame.setResizable(false);
        frame.setContentPane(panel);


        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(programName);
        panel.add(programAuthor);

        panel.add(Box.createGlue());
        panel.add(getCenteredLabel("Circuit Simulator by George Tsakiridis"));
        panel.add(getCenteredLabel(Localization.getString("version") + ": " + ChipSimulator.PROGRAM_VERSION_STRING + " (" + Localization.getString("build") + " " + ChipSimulator.PROGRAM_VERSION + ")"));
        panel.add(getCenteredLabel("(C)2021 Tsaky. All rights reserved."));
        panel.add(Box.createGlue());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JLabel getCenteredLabel(String text){
        JLabel jLabel = new JLabel(text);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return jLabel;
    }

}
