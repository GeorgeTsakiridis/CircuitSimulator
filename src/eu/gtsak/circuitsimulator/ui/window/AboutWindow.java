package eu.gtsak.circuitsimulator.ui.window;

import eu.gtsak.circuitsimulator.CircuitSimulator;
import eu.gtsak.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.awt.*;

public class AboutWindow {

    public AboutWindow(JFrame mainFrame){
        Dimension size = new Dimension(460, 260);

        JDialog frame = new JDialog(mainFrame, Localization.getString("about"));
        JPanel panel = new JPanel();

        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setSize(size);
        frame.setResizable(false);

        JLabel programName = getCenteredLabel("Circuit Simulator");
        programName.setFont(programName.getFont().deriveFont(50f));

        JLabel programAuthor = new JLabel("by George Tsakiridis");

        frame.setContentPane(panel);


        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(programName);
        panel.add(programAuthor);

        panel.add(Box.createGlue());
        panel.add(getCenteredLabel("Circuit Simulator by George Tsakiridis"));
        panel.add(getCenteredLabel(Localization.getString("version") + ": " + CircuitSimulator.PROGRAM_VERSION_STRING + " (" + Localization.getString("build") + " " + CircuitSimulator.PROGRAM_VERSION + ")"));
        panel.add(getCenteredLabel("(C) Tsaky. All rights reserved."));
        panel.add(Box.createGlue());

        frame.setLocationRelativeTo(mainFrame);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JLabel getCenteredLabel(String text){
        JLabel jLabel = new JLabel(text);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return jLabel;
    }

}
