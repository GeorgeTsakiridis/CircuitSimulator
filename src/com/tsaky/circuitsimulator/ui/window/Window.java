package com.tsaky.circuitsimulator.ui.window;

import com.tsaky.circuitsimulator.EmulationAction;
import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.mouse.MouseMode;
import com.tsaky.circuitsimulator.ui.LineViewMode;
import com.tsaky.circuitsimulator.ui.PinViewMode;
import com.tsaky.circuitsimulator.ui.ResourceManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Window {

    private final ViewportPanel viewportPanel;

    private final Handler handler;

    private final JFrame mainFrame;
    private final JFrame componentInfoFrame;

    private final JLabel infoLabel = new JLabel("Info Label");

    private final JSlider simulationSpeedSlider;

    private final ArrayList<MouseModeChangeButton> mouseModeChangeButtons = new ArrayList<>();
    private final MouseModeChangeButton cameraButton = new MouseModeChangeButton(MouseMode.CAMERA, ResourceManager.getResource("mf_viewport_move"), "Move Camera");
    private final MouseModeChangeButton textButton = new MouseModeChangeButton(MouseMode.TEXT, ResourceManager.getResource("mf_text"), "Edit Texts");
    private final MouseModeChangeButton toggleButton = new MouseModeChangeButton(MouseMode.TOGGLE, ResourceManager.getResource("mf_toggle"), "Toggle Component");
    private final MouseModeChangeButton moveButton = new MouseModeChangeButton(MouseMode.MOVE, ResourceManager.getResource("mf_move"), "Move Component");
    private final MouseModeChangeButton addButton = new MouseModeChangeButton(MouseMode.ADD, ResourceManager.getResource("mf_add"), "Add Component");
    private final MouseModeChangeButton linkButton = new MouseModeChangeButton(MouseMode.LINK, ResourceManager.getResource("mf_link"), "Link Pins");
    private final MouseModeChangeButton removeButton = new MouseModeChangeButton(MouseMode.REMOVE, ResourceManager.getResource("mf_remove"), "Remove Component");

    private final ImageButton gridSnapButton = new ImageButton(ResourceManager.getResource("mf_snap_unlocked"));

    private final ArrayList<EmulationChangeButton> emulationChangeButtons = new ArrayList<>();
    private final EmulationChangeButton startEmulationButton = new EmulationChangeButton(EmulationAction.START, ResourceManager.getResource("sim_start"), "Start Simulation");
    private final EmulationChangeButton stopEmulationButton = new EmulationChangeButton(EmulationAction.STOP, ResourceManager.getResource("sim_stop"), "Stop Simulation");
    private final EmulationChangeButton stepEmulationButton = new EmulationChangeButton(EmulationAction.STEP, ResourceManager.getResource("sim_step"), "Step Simulation");

    private final ArrayList<LineViewChangeButton> lineViewChangeButtons = new ArrayList<>();
    private final LineViewChangeButton normalLineViewButton = new LineViewChangeButton(LineViewMode.NORMAL, ResourceManager.getResource("view_line_normal"), "Normal Lines View");
    private final LineViewChangeButton statusLineViewButton = new LineViewChangeButton(LineViewMode.STATUS, ResourceManager.getResource("view_line_status"), "Lines Status View");
    //private ViewChangeButton powerViewButton = new ViewChangeButton(ViewMode.POWER_STATUS, ResourceManager.getResource("view_line_power"), "View/Hide Power Status");

    private final ArrayList<PinViewChangeButton> pinViewChangeButtons = new ArrayList<>();
    private final PinViewChangeButton normalPinViewButton = new PinViewChangeButton(PinViewMode.NORMAL, ResourceManager.getResource("view_pin_normal"), "Normal Pins View");
    private final PinViewChangeButton statusPinViewButton = new PinViewChangeButton(PinViewMode.STATUS, ResourceManager.getResource("view_pin_status"), "Pins Status View");
    private final PinViewChangeButton typePinViewButton = new PinViewChangeButton(PinViewMode.TYPE, ResourceManager.getResource("view_pin_type"), "Pins Type View");

    private final ImageButton chipNameToggleButton = new ImageButton(ResourceManager.getResource("view_chip_custom_name"));
    private final ImageButton gridToggleButton = new ImageButton(ResourceManager.getResource("view_grid_toggle"));
    private final ImageButton zoomInButton = new ImageButton(ResourceManager.getResource("view_zoom_in"));
    private final ImageButton zoomOutButton = new ImageButton(ResourceManager.getResource("view_zoom_out"));
    private final ImageButton zoomResetButton = new ImageButton(ResourceManager.getResource("view_zoom_reset"));
    private final ImageButton showComponentInfoButton = new ImageButton(ResourceManager.getResource("view_show_component_info"));

    private boolean renderRealName = false;
    private boolean mouseGridSnap = false;

    @SuppressWarnings("unchecked")
    public Window(Handler handler, ViewportPanel viewportPanel, ComponentInfoPanel componentInfoPanel){

        this.handler = handler;
        this.viewportPanel = viewportPanel;

        componentInfoFrame = new JFrame("Component Pinout");
        componentInfoFrame.setAlwaysOnTop(true);
        componentInfoFrame.setSize(380, 400);
        componentInfoFrame.setMinimumSize(new Dimension(380, 400));
        componentInfoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        componentInfoFrame.add(componentInfoPanel);

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

        JList<String> componentsList = new JList<>(ChipManager.getAllChipNames());
        componentsList.setBackground(UIManager.getColor("background"));
        componentsList.addListSelectionListener(e -> handler.setSelectedComponent(((JList<String>) (e.getSource())).getSelectedValue()));

        simulationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 20, 1000, 500);
        simulationSpeedSlider.setPreferredSize(new Dimension(160, 50));
        simulationSpeedSlider.addChangeListener(e -> handler.setSimulationSpeed(((JSlider)e.getSource()).getValue()));
        simulationSpeedSlider.setToolTipText("Update interval in ms");

        normalLineViewButton.setEnabled(false);
        normalPinViewButton.setEnabled(false);

        ImageButton newButton = new ImageButton(ResourceManager.getResource("proj_new"));
        newButton.setToolTipText("New Project");
        ImageButton saveButton = new ImageButton(ResourceManager.getResource("proj_save"));
        saveButton.setToolTipText("Save Project");
        ImageButton loadButton = new ImageButton(ResourceManager.getResource("proj_load"));
        loadButton.setToolTipText("Open Project");

        upProjectPanel.add(newButton);
        upProjectPanel.add(saveButton);
        upProjectPanel.add(loadButton);
        for(MouseModeChangeButton button : mouseModeChangeButtons){
            upMouseFunctionPanel.add(button);
        }
        cameraButton.setEnabled(false);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);

        upMouseFunctionPanel.add(separator);

        Dimension dimension = separator.getPreferredSize();
        dimension.height = 30;
        separator.setPreferredSize(dimension);

        upMouseFunctionPanel.add(gridSnapButton);

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

        chipNameToggleButton.setToolTipText("Switch between custom and real component names");
        gridToggleButton.setToolTipText("Show/Hide the grid");
        zoomInButton.setToolTipText("Zoom In");
        zoomOutButton.setToolTipText("Zoom Out");
        zoomResetButton.setToolTipText("Reset Camera");
        showComponentInfoButton.setToolTipText("Show the Component Info Window");

        mainFrame.setSize(1200, 480);
        mainFrame.setMinimumSize(new Dimension(1200, 350));
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.setFocusable(true);
        mainFrame.setFocusTraversalKeysEnabled(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(viewportPanel, "Center");
        mainFrame.add(leftPanel, "West");
        mainFrame.add(upPanel, "North");
        mainFrame.add(downPanel, "South");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        int x = Toolkit.getDefaultToolkit().getScreenSize().width/2 - mainFrame.getSize().width/2;
        int y = mainFrame.getLocationOnScreen().y;
        mainFrame.setLocation(x, y);

        newButton.addActionListener(newProjectAction);
        saveButton.addActionListener(saveProjectAction);
        loadButton.addActionListener(loadProjectAction);
        gridSnapButton.addActionListener(mouseGridSnapModeAction);
        chipNameToggleButton.addActionListener(chipNameToggleAction);
        gridToggleButton.addActionListener(gridToggleAction);
        zoomOutButton.addActionListener(getZoomAction(0));
        zoomInButton.addActionListener(getZoomAction(1));
        zoomResetButton.addActionListener(getZoomAction(2));
        showComponentInfoButton.addActionListener(showComponentInfoWindowAction);

        bind(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "New Project", newProjectAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "Save Project", saveProjectAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "Open Project", loadProjectAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "Move Viewport", getMouseFunctionChangeAction(MouseMode.CAMERA));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "Edit Display Text", getMouseFunctionChangeAction(MouseMode.TEXT));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "Toggle", getMouseFunctionChangeAction(MouseMode.TOGGLE));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK), "Move", getMouseFunctionChangeAction(MouseMode.MOVE));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "Add", getMouseFunctionChangeAction(MouseMode.ADD));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "Link", getMouseFunctionChangeAction(MouseMode.LINK));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Remove", getMouseFunctionChangeAction(MouseMode.REMOVE));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "Toggle Mouse Grid Snap Mode", mouseGridSnapModeAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "Start Simulation", getChangeSimulationAction(EmulationAction.START));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "Stop Simulation", getChangeSimulationAction(EmulationAction.STOP));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "Step Simulation", getChangeSimulationAction(EmulationAction.STEP));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "Decrease Speed Simulation", getSimulationSpeedSliderChangeAction(false));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "Increase Speed Simulation", getSimulationSpeedSliderChangeAction(true));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK), "Normal Lines View", getChangeLineViewAction(LineViewMode.NORMAL));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.ALT_DOWN_MASK), "Status Lines View", getChangeLineViewAction(LineViewMode.STATUS));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK), "Normal Pins View", getChangePinViewAction(PinViewMode.NORMAL));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK), "Status Pins View", getChangePinViewAction(PinViewMode.STATUS));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK), "Type Pins View", getChangePinViewAction(PinViewMode.TYPE));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK), "Toggle Chip Name", chipNameToggleAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK), "Show Hide Grid", gridToggleAction);
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), "Zoom In 1", getZoomAction(1));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK), "Zoom In 2", getZoomAction(1));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "Zoom Out 1", getZoomAction(0));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), "Zoom Out 2", getZoomAction(0));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "Reset", getZoomAction(2));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK), "Show Component Info Window", showComponentInfoWindowAction);

    }

    private void bind(KeyStroke key, String string, Action action) {
        viewportPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, string);
        viewportPanel.getActionMap().put(string, action);
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

    public void enableOtherEmulationButtons(EmulationAction emulationAction) {
        for (EmulationChangeButton button : emulationChangeButtons) {
            button.setEnabled(button.emulationAction != emulationAction);
        }
    }

    public void reset(){
        enableOtherEmulationButtons(EmulationAction.STOP);
        for (MouseModeChangeButton mouseModeChangeButton : mouseModeChangeButtons) {
            mouseModeChangeButton.setEnabled(mouseModeChangeButton.mouseMode != MouseMode.CAMERA);
        }

        simulationSpeedSlider.setValue(500);

        for(LineViewChangeButton button : lineViewChangeButtons)button.setEnabled(true);
        for(PinViewChangeButton button : pinViewChangeButtons)button.setEnabled(true);

        normalLineViewButton.setEnabled(false);
        normalPinViewButton.setEnabled(false);

        chipNameToggleButton.setIcon(ResourceManager.getResource("view_chip_custom_name"));
        renderRealName = false;
        viewportPanel.setRenderChipRealName(false);

        gridSnapButton.setIcon(ResourceManager.getResource("mf_snap_unlocked"));
        mouseGridSnap = false;
        viewportPanel.setMouseSnapEnabled(false);
    }

    private AbstractAction newProjectAction = new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(getValidation()){
                handler.reset();
            }
        }
    };

    private AbstractAction saveProjectAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Circuit Simulation Save File (*.cssf)", "cssf"));
                fileChooser.showSaveDialog(mainFrame);

                if(fileChooser.getSelectedFile() != null) handler.saveToFile(fileChooser.getSelectedFile());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    };

    private AbstractAction loadProjectAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
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
        }
    };

    private AbstractAction getMouseFunctionChangeAction(MouseMode mouseMode) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.setMouseMode(mouseMode);
                for (MouseModeChangeButton button : mouseModeChangeButtons) {
                    button.setEnabled(button.mouseMode != mouseMode);
                }
            }
        };
    }

    private AbstractAction mouseGridSnapModeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mouseGridSnap = !mouseGridSnap;
            viewportPanel.setMouseSnapEnabled(mouseGridSnap);
            if(mouseGridSnap){
                gridSnapButton.setIcon(ResourceManager.getResource("mf_snap_locked"));
            }else{
                gridSnapButton.setIcon(ResourceManager.getResource("mf_snap_unlocked"));
            }
        }
    };

    private AbstractAction getChangeSimulationAction(EmulationAction emulationAction) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.setEmulationMode(emulationAction);
            }
        };
    }

    private AbstractAction getSimulationSpeedSliderChangeAction(boolean increase) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int simulationSpeed = simulationSpeedSlider.getValue();
                if(increase) {
                    simulationSpeed += 50;
                    if(simulationSpeed > simulationSpeedSlider.getMaximum()){
                        simulationSpeed = simulationSpeedSlider.getMaximum();
                    }
                }else{
                    simulationSpeed -= 50;
                    if(simulationSpeed < simulationSpeedSlider.getMinimum()){
                        simulationSpeed = simulationSpeedSlider.getMinimum();
                    }
                }

                handler.setSimulationSpeed(simulationSpeed);
                simulationSpeedSlider.setValue(simulationSpeed);
            }
        };
    }

    private AbstractAction getChangeLineViewAction(LineViewMode lineViewMode) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewportPanel.lineViewMode = lineViewMode;

                for (LineViewChangeButton button: lineViewChangeButtons) {
                    button.setEnabled(button.lineViewMode != lineViewMode);
                }
            }
        };
    }

    private AbstractAction getChangePinViewAction(PinViewMode pinViewMode) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewportPanel.pinViewMode = pinViewMode;

                for (PinViewChangeButton button: pinViewChangeButtons) {
                    button.setEnabled(button.pinViewMode != pinViewMode);
                }
            }
        };
    }

    private AbstractAction chipNameToggleAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            renderRealName = !renderRealName;
            viewportPanel.setRenderChipRealName(renderRealName);
            if(renderRealName){
                chipNameToggleButton.setIcon(ResourceManager.getResource("view_chip_real_name"));
            }else{
                chipNameToggleButton.setIcon(ResourceManager.getResource("view_chip_custom_name"));
            }
        }
    };

    private AbstractAction gridToggleAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewportPanel.toggleGrid();
        }
    };

    private AbstractAction getZoomAction(int action){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (action){
                    case 0 -> viewportPanel.decreaseScale();
                    case 1 -> viewportPanel.increaseScale();
                    case 2 -> viewportPanel.resetOffsetAndScale();
                }
            }
        };
    }

    private AbstractAction showComponentInfoWindowAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            componentInfoFrame.setVisible(true);
        }
    };

    private class EmulationChangeButton extends ImageButton{
        private final EmulationAction emulationAction;

        public EmulationChangeButton(EmulationAction emulationAction, ImageIcon icon, String tooltip){
            super(icon);
            this.emulationAction = emulationAction;
            addActionListener(getChangeSimulationAction(emulationAction));
            setToolTipText(tooltip);
            emulationChangeButtons.add(this);
        }
    }

    private class LineViewChangeButton extends ImageButton{
        private final LineViewMode lineViewMode;

        public LineViewChangeButton(LineViewMode lineViewMode, ImageIcon icon, String tooltip){
            super(icon);
            this.lineViewMode = lineViewMode;
            addActionListener(getChangeLineViewAction(lineViewMode));
            setToolTipText(tooltip);
            lineViewChangeButtons.add(this);
        }
    }

    private class PinViewChangeButton extends ImageButton{
        private final PinViewMode pinViewMode;

        public PinViewChangeButton(PinViewMode pinViewMode, ImageIcon icon, String tooltip){
            super(icon);
            this.pinViewMode = pinViewMode;
            addActionListener(getChangePinViewAction(pinViewMode));
            setToolTipText(tooltip);
            pinViewChangeButtons.add(this);
        }
    }

    private class MouseModeChangeButton extends ImageButton{
        private final MouseMode mouseMode;

        public MouseModeChangeButton(MouseMode mouseMode, ImageIcon icon, String tooltip){
            super(icon);
            this.mouseMode = mouseMode;
            addActionListener(getMouseFunctionChangeAction(mouseMode));
            setToolTipText(tooltip);
            mouseModeChangeButtons.add(this);
        }

    }

    private static class ImageButton extends JButton{
        public ImageButton(ImageIcon icon){
            setIcon(icon);
            setPreferredSize(new Dimension(30, 30));
        }
    }
}
