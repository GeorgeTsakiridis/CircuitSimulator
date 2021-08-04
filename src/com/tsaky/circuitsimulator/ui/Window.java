package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.EmulationAction;
import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.mouse.MouseMode;
import com.tsaky.circuitsimulator.chip.ChipManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Window implements KeyListener {

    private final Handler handler;
    private final ViewportPanel viewportPanel;
    private final ComponentInfoPanel componentInfoPanel;

    private final JFrame mainFrame;
    private final JFrame componentInfoFrame;

    private final JLabel infoLabel = new JLabel("Info Label");

    private final JSlider simulationSpeedSlider;

    private final ImageButton newButton = new ImageButton("new.png");
    private final ImageButton saveButton = new ImageButton("save.png");
    private final ImageButton loadButton = new ImageButton("load.png");

    private final ArrayList<MouseModeChangeButton> mouseModeChangeButtons = new ArrayList<>();
    private final MouseModeChangeButton cameraButton = new MouseModeChangeButton(MouseMode.CAMERA, "cameraMove.png", "Move Camera");
    private final MouseModeChangeButton textButton = new MouseModeChangeButton(MouseMode.TEXT, "text.png", "Edit Texts");
    private final MouseModeChangeButton toggleButton = new MouseModeChangeButton(MouseMode.TOGGLE, "toggle.png", "Toggle Component");
    private final MouseModeChangeButton moveButton = new MouseModeChangeButton(MouseMode.MOVE, "move.png", "Move Component");
    private final MouseModeChangeButton addButton = new MouseModeChangeButton(MouseMode.ADD, "add.png", "Add Component");
    private final MouseModeChangeButton linkButton = new MouseModeChangeButton(MouseMode.LINK, "link.png", "Link Pins");
    private final MouseModeChangeButton removeButton = new MouseModeChangeButton(MouseMode.REMOVE, "remove.png", "Remove Component");

    private final ArrayList<EmulationChangeButton> emulationChangeButtons = new ArrayList<>();
    private final EmulationChangeButton startEmulationButton = new EmulationChangeButton(EmulationAction.START, "simulationStart.png", "Start Simulation");
    private final EmulationChangeButton stopEmulationButton = new EmulationChangeButton(EmulationAction.STOP, "simulationStop.png", "Start Simulation");
    private final EmulationChangeButton stepEmulationButton = new EmulationChangeButton(EmulationAction.STEP, "simulationStep.png", "Step Simulation");

    private final ArrayList<LineViewChangeButton> lineViewChangeButtons = new ArrayList<>();
    private final LineViewChangeButton normalLineViewButton = new LineViewChangeButton(LineViewMode.NORMAL, "normalView.png", "Normal Lines View");
    private final LineViewChangeButton statusLineViewButton = new LineViewChangeButton(LineViewMode.STATUS, "lineView.png", "Lines Status View");
    //private ViewChangeButton powerViewButton = new ViewChangeButton(ViewMode.POWER_STATUS, "powerView.png", "View/Hide Power Status");

    private final ArrayList<PinViewChangeButton> pinViewChangeButtons = new ArrayList<>();
    private final PinViewChangeButton normalPinViewButton = new PinViewChangeButton(PinViewMode.NORMAL, "normalPinView.png", "Normal Pins View");
    private final PinViewChangeButton statusPinViewButton = new PinViewChangeButton(PinViewMode.STATUS, "statusPinView.png", "Pins Status View");
    private final PinViewChangeButton typePinViewButton = new PinViewChangeButton(PinViewMode.TYPE, "typePinView.png", "Pins Type View");

    private final ImageButton chipNameToggleButton = new ImageButton("chipNameSwitch.png");
    private final ImageButton gridToggleButton = new ImageButton("gridToggle.png");
    private final ImageButton zoomInButton = new ImageButton("zoomIn.png");
    private final ImageButton zoomOutButton = new ImageButton("zoomOut.png");
    private final ImageButton zoomResetButton = new ImageButton("zoomReset.png");
    private final ImageButton showComponentInfoButton = new ImageButton("showComponentInfo.png");

    @SuppressWarnings("unchecked")
    public Window(Handler handler, ViewportPanel viewportPanel, ComponentInfoPanel componentInfoPanel){

        this.handler = handler;
        this.viewportPanel = viewportPanel;
        this.componentInfoPanel = componentInfoPanel;

        componentInfoFrame = new JFrame("Component Pinout");
        componentInfoFrame.setAlwaysOnTop(true);
        componentInfoFrame.setSize(380, 400);
        componentInfoFrame.setMinimumSize(new Dimension(380, 400));
        componentInfoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        componentInfoFrame.add(componentInfoPanel);

        normalLineViewButton.setEnabled(false);
        normalPinViewButton.setEnabled(false);

        JList<String> componentsList = new JList<>(ChipManager.getAllChipNames());
        componentsList.setBackground(UIManager.getColor("background"));
        componentsList.addListSelectionListener(e -> handler.setSelectedComponent(((JList<String>) (e.getSource())).getSelectedValue()));

        simulationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 20, 1000, 500);
        simulationSpeedSlider.setPreferredSize(new Dimension(160, 50));
        simulationSpeedSlider.addChangeListener(e -> handler.setSimulationSpeed(((JSlider)e.getSource()).getValue()));
        simulationSpeedSlider.setToolTipText("Update interval in ms");

        JPanel upPanel = new JPanel();
        JScrollPane leftPanel = new JScrollPane();
        JPanel rightPanel = new JPanel();
        JPanel downPanel = new JPanel();

        JPanel upProjectPanel = new JPanel();
        JPanel upMouseFunctionPanel = new JPanel();
        JPanel upSimulationPanel = new JPanel();
        JPanel upViewPanel = new JPanel();

        upProjectPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Project"));
        upMouseFunctionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mouse Function"));
        upSimulationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Simulation"));
        upViewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "View"));

        newButton.setToolTipText("New Project");
        saveButton.setToolTipText("Save Project");
        loadButton.setToolTipText("Open Project");

        upProjectPanel.add(newButton);
        upProjectPanel.add(saveButton);
        upProjectPanel.add(loadButton);
        for(MouseModeChangeButton button : mouseModeChangeButtons){
            upMouseFunctionPanel.add(button);
        }
        cameraButton.setEnabled(false);
        for(EmulationChangeButton button : emulationChangeButtons){
            upSimulationPanel.add(button);
        }

        upSimulationPanel.add(simulationSpeedSlider);
        stopEmulationButton.setEnabled(false);
        addViewButtonsToPanel(upViewPanel);

        upPanel.add(upProjectPanel);
        upPanel.add(upMouseFunctionPanel);
        upPanel.add(upSimulationPanel);
        upPanel.add(upViewPanel);

        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Components"));
        leftPanel.setFocusable(false);
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setViewportView(componentsList);

        //rightPanel.add(componentInfoPanel);

        downPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Component Info"));
        downPanel.add(infoLabel);

        mainFrame = new JFrame("Circuit Simulator by George Tsakiridis");

        newButton.addActionListener(e -> {
            if(getValidation()){
                handler.reset();
            }
        });

        saveButton.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Circuit Simulation Save File (*.cssf)", "cssf"));
                fileChooser.showSaveDialog(mainFrame);

                if(fileChooser.getSelectedFile() != null) handler.saveToFile(fileChooser.getSelectedFile());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        loadButton.addActionListener(e -> {
            if(getValidation()) {
                try {

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Circuit Simulation Save File (*.cssf)", "cssf"));
                    fileChooser.showOpenDialog(mainFrame);
                    if (fileChooser.getSelectedFile() != null)handler.loadFromFile(fileChooser.getSelectedFile());
                } catch (IOException IOException) {
                    IOException.printStackTrace();
                }
            }
        });

        chipNameToggleButton.setToolTipText("Switch between custom and real component names");
        gridToggleButton.setToolTipText("Show/Hide the grid");
        zoomInButton.setToolTipText("Zoom In");
        zoomOutButton.setToolTipText("Zoom Out");
        zoomResetButton.setToolTipText("Reset Camera");
        showComponentInfoButton.setToolTipText("Show the Component Info Window");

        chipNameToggleButton.addActionListener(e -> viewportPanel.switchChipName());
        gridToggleButton.addActionListener(e -> viewportPanel.toggleGrid());
        zoomInButton.addActionListener(e -> viewportPanel.increaseScale());
        zoomOutButton.addActionListener(e -> viewportPanel.decreaseScale());
        zoomResetButton.addActionListener(e -> viewportPanel.resetOffsetAndScale());
        showComponentInfoButton.addActionListener(e -> {
            if(!componentInfoFrame.isVisible()){
                componentInfoFrame.setVisible(true);
            }
        });

        mainFrame.setSize(1100, 480);
        mainFrame.setMinimumSize(new Dimension(1100, 350));
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.setFocusable(true);
        mainFrame.setFocusTraversalKeysEnabled(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(viewportPanel, "Center");
        mainFrame.add(leftPanel, "West");
        mainFrame.add(upPanel, "North");
        mainFrame.add(downPanel, "South");
        mainFrame.addKeyListener(this);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        int x = Toolkit.getDefaultToolkit().getScreenSize().width/2 - mainFrame.getSize().width/2;
        int y = mainFrame.getLocationOnScreen().y;
        mainFrame.setLocation(x, y);

    }

    private boolean getValidation(){
        return JOptionPane.showConfirmDialog(null, "Any unsaved progress will be lost.\nAre you sure you want to continue?") == 0;
    }

    private void addViewButtonsToPanel(JPanel panel){
        panel.add(normalLineViewButton);
        panel.add(statusLineViewButton);
        panel.add(normalPinViewButton);
        panel.add(statusPinViewButton);
        panel.add(typePinViewButton);
        panel.add(chipNameToggleButton);
        panel.add(gridToggleButton);
        panel.add(zoomOutButton);
        panel.add(zoomInButton);
        panel.add(zoomResetButton);
        panel.add(showComponentInfoButton);
    }

    public void setChipDescription(Chip chip){
        infoLabel.setText(chip.getDescription());
    }

    private void enableOtherMouseButtons(MouseMode mouseMode){
        for(MouseModeChangeButton mouseModeChangeButton : mouseModeChangeButtons){
            if(mouseModeChangeButton.mouseMode != mouseMode){
                mouseModeChangeButton.setEnabled(true);
            }
        }
    }

    public void enableOtherEmulationButtons(EmulationAction emulationAction){
        if(emulationAction == EmulationAction.STOP){
            stopEmulationButton.setEnabled(false);
            startEmulationButton.setEnabled(true);
            stepEmulationButton.setEnabled(true);
        }else{
            stopEmulationButton.setEnabled(true);
            startEmulationButton.setEnabled(false);
            stepEmulationButton.setEnabled(false);
        }
    }

    public void reset(){
        enableOtherEmulationButtons(EmulationAction.STOP);
        enableOtherMouseButtons(MouseMode.CAMERA);
        cameraButton.setEnabled(false);
        simulationSpeedSlider.setValue(500);

        for(LineViewChangeButton button : lineViewChangeButtons)button.setEnabled(true);
        for(PinViewChangeButton button : pinViewChangeButtons)button.setEnabled(true);

        normalLineViewButton.setEnabled(false);
        normalPinViewButton.setEnabled(false);

    }

    private class EmulationChangeButton extends ImageButton{
        private final EmulationAction emulationAction;

        public EmulationChangeButton(EmulationAction emulationAction, String assetName, String tooltip){
            super(assetName);
            this.emulationAction = emulationAction;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
            emulationChangeButtons.add(this);
        }

        public void actionPerformed(ActionEvent e){
            handler.setEmulationMode(emulationAction);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class LineViewChangeButton extends ImageButton{
        private final LineViewMode lineViewMode;

        public LineViewChangeButton(LineViewMode lineViewMode, String assetName, String tooltip){
            super(assetName);
            this.lineViewMode = lineViewMode;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
            lineViewChangeButtons.add(this);
        }

        public void actionPerformed(ActionEvent e){
            ViewportPanel.lineViewMode = lineViewMode;
            this.setEnabled(false);

            for(LineViewChangeButton button : lineViewChangeButtons){
                if(button != this){
                    button.setEnabled(true);
                }
            }
        }

    }

    private class PinViewChangeButton extends ImageButton{
        private final PinViewMode pinViewMode;

        public PinViewChangeButton(PinViewMode pinViewMode, String assetName, String tooltip){
            super(assetName);
            this.pinViewMode = pinViewMode;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
            pinViewChangeButtons.add(this);
        }

        public void actionPerformed(ActionEvent e){
            ViewportPanel.pinViewMode = pinViewMode;
            this.setEnabled(false);

            for(PinViewChangeButton button : pinViewChangeButtons){
                if(button != this){
                    button.setEnabled(true);
                }
            }
        }

    }

    private class MouseModeChangeButton extends ImageButton{
        private final MouseMode mouseMode;

        public MouseModeChangeButton(MouseMode mouseMode, String assetName, String tooltip){
            super(assetName);
            this.mouseMode = mouseMode;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
            mouseModeChangeButtons.add(this);
        }

        public void actionPerformed(ActionEvent e) {
            handler.setMouseMode(mouseMode);
            enableOtherMouseButtons(mouseMode);
            ((JButton)(e.getSource())).setEnabled(false);
        }
    }

    private static class ImageButton extends JButton{
        public ImageButton(String assetName){
            try {
                this.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/buttons/" + assetName)))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(new Dimension(30, 30));
        }
    }

}
