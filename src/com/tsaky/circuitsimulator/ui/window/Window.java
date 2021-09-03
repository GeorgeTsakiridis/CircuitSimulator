package com.tsaky.circuitsimulator.ui.window;

import com.tsaky.circuitsimulator.*;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.logic.CSActionsManager;
import com.tsaky.circuitsimulator.logic.Handler;
import com.tsaky.circuitsimulator.ui.Localization;
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
import java.net.URI;
import java.util.ArrayList;

public class Window {

    private final ViewportPanel viewportPanel;
    private final Handler handler;

    private final JFrame mainFrame;
    private final JFrame componentInfoFrame;

    private final JLabel infoLabel = new JLabel(" ");
    private final JSlider simulationSpeedSlider;

    private final ArrayList<MouseModeChangeButton> mouseModeChangeButtons = new ArrayList<>();
    private final ArrayList<EmulationChangeButton> emulationChangeButtons = new ArrayList<>();
    private final ArrayList<LineViewChangeButton> lineViewChangeButtons = new ArrayList<>();
    private final ArrayList<PinViewChangeButton> pinViewChangeButtons = new ArrayList<>();

    private final ImageButton gridSnapButton = new ImageButton(ResourceManager.getResource("mf_snap_unlocked"));
    private final ImageButton chipNameToggleButton = new ImageButton(ResourceManager.getResource("view_chip_custom_name"));

    private boolean renderRealName = false;
    private boolean mouseGridSnap = false;

    private JMenu projectMenu = new JMenu(Localization.getString("project"));
    private JMenu viewMenu = new JMenu(Localization.getString("view"));
    private JMenu linesViewSubMenu = new JMenu(Localization.getString("lines"));
    private JMenu pinsViewSubMenu = new JMenu(Localization.getString("pins"));
    private JMenu simulationMenu = new JMenu(Localization.getString("simulation"));
    private JMenu helpMenu = new JMenu(Localization.getString("help"));


    @SuppressWarnings("unchecked")
    public Window(Handler handler, ViewportPanel viewportPanel, ComponentInfoPanel componentInfoPanel){

        this.handler = handler;
        this.viewportPanel = viewportPanel;

        componentInfoFrame = new JFrame(Localization.getString("component_pinout"));
        componentInfoFrame.setAlwaysOnTop(true);
        componentInfoFrame.setSize(380, 400);
        componentInfoFrame.setMinimumSize(new Dimension(380, 400));
        componentInfoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        componentInfoFrame.add(componentInfoPanel);

        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.CAMERA, ResourceManager.getResource("mf_viewport_move"), Localization.getString("move_viewport")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.TEXT, ResourceManager.getResource("mf_text"), Localization.getString("edit_text")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.TOGGLE, ResourceManager.getResource("mf_toggle"), Localization.getString("toggle_component")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.MOVE, ResourceManager.getResource("mf_move"), Localization.getString("move_component")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.ADD, ResourceManager.getResource("mf_add"), Localization.getString("add_component")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.LINK, ResourceManager.getResource("mf_link"), Localization.getString("link_pins")));
        mouseModeChangeButtons.add(new MouseModeChangeButton(MouseMode.REMOVE, ResourceManager.getResource("mf_remove"), Localization.getString("remove_component")));
        mouseModeChangeButtons.get(0).setEnabled(false);

        emulationChangeButtons.add(new EmulationChangeButton(EmulationAction.START, ResourceManager.getResource("sim_start"), Localization.getString("start_simulation")));
        emulationChangeButtons.add(new EmulationChangeButton(EmulationAction.STOP, ResourceManager.getResource("sim_stop"), Localization.getString("stop_simulation")));
        emulationChangeButtons.add(new EmulationChangeButton(EmulationAction.STEP, ResourceManager.getResource("sim_step"), Localization.getString("step_simulation")));
        emulationChangeButtons.get(1).setEnabled(false);

        lineViewChangeButtons.add(new LineViewChangeButton(LineViewMode.NORMAL, ResourceManager.getResource("view_line_normal"), Localization.getString("lines_view_normal")));
        lineViewChangeButtons.add(new LineViewChangeButton(LineViewMode.STATUS, ResourceManager.getResource("view_line_status"), Localization.getString("lines_view_status")));
        //lineViewChangeButtons.add(new LineViewChangeButton(LineViewMode.POWER_STATUS, ResourceManager.getResource("view_line_power"), "Lines Power View"));
        lineViewChangeButtons.get(0).setEnabled(false);

        pinViewChangeButtons.add(new PinViewChangeButton(PinViewMode.NORMAL, ResourceManager.getResource("view_pin_normal"), Localization.getString("pins_view_normal")));
        pinViewChangeButtons.add(new PinViewChangeButton(PinViewMode.STATUS, ResourceManager.getResource("view_pin_status"), Localization.getString("pins_view_status")));
        pinViewChangeButtons.add(new PinViewChangeButton(PinViewMode.TYPE, ResourceManager.getResource("view_pin_type"), Localization.getString("pins_view_type")));
        pinViewChangeButtons.get(0).setEnabled(false);

        JPanel upPanel = new JPanel();
        JScrollPane leftPanel = new JScrollPane();
        JPanel rightPanel = new JPanel();
        JPanel downPanel = new JPanel();

        JPanel upProjectPanel = new JPanel();
        JPanel upMouseFunctionPanel = new JPanel();
        JPanel upSimulationPanel = new JPanel();
        JPanel upViewPanel = new JPanel();

        upProjectPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("project")));
        upMouseFunctionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("mouse_function")));
        upSimulationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("simulation")));
        upViewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("view")));

        JList<String> componentsList = new JList<>(ChipManager.getAllChipNames());
        componentsList.setBackground(UIManager.getColor("background"));
        componentsList.addListSelectionListener(e -> handler.setSelectedComponent(((JList<String>) (e.getSource())).getSelectedValue()));

        simulationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 20, 1000, 500);
        simulationSpeedSlider.setPreferredSize(new Dimension(160, 50));
        simulationSpeedSlider.addChangeListener(e -> handler.setSimulationSpeed(((JSlider)e.getSource()).getValue()));
        simulationSpeedSlider.setToolTipText(Localization.getString("update_interval_in_ms"));

        ImageButton newButton = new ImageButton(ResourceManager.getResource("proj_new"));
        newButton.setToolTipText(Localization.getString("new_project"));
        ImageButton saveButton = new ImageButton(ResourceManager.getResource("proj_save"));
        saveButton.setToolTipText(Localization.getString("save_project"));
        ImageButton loadButton = new ImageButton(ResourceManager.getResource("proj_load"));
        loadButton.setToolTipText(Localization.getString("open_project"));

        upProjectPanel.add(newButton);
        upProjectPanel.add(saveButton);
        upProjectPanel.add(loadButton);
        for(MouseModeChangeButton button : mouseModeChangeButtons){
            upMouseFunctionPanel.add(button);
        }

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

        for (LineViewChangeButton button : lineViewChangeButtons)upViewPanel.add(button);
        for (PinViewChangeButton button : pinViewChangeButtons)upViewPanel.add(button);

        ImageButton gridToggleButton = new ImageButton(ResourceManager.getResource("view_grid_toggle"));
        ImageButton zoomInButton = new ImageButton(ResourceManager.getResource("view_zoom_in"));
        ImageButton zoomOutButton = new ImageButton(ResourceManager.getResource("view_zoom_out"));
        ImageButton zoomResetButton = new ImageButton(ResourceManager.getResource("view_zoom_reset"));
        ImageButton showComponentInfoButton = new ImageButton(ResourceManager.getResource("view_show_component_info"));

        upViewPanel.add(chipNameToggleButton);
        upViewPanel.add(gridToggleButton);
        upViewPanel.add(zoomOutButton);
        upViewPanel.add(zoomInButton);
        upViewPanel.add(zoomResetButton);
        upViewPanel.add(showComponentInfoButton);

        upPanel.add(upProjectPanel);
        upPanel.add(upMouseFunctionPanel);
        upPanel.add(upSimulationPanel);
        upPanel.add(upViewPanel);

        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("components")));
        leftPanel.setFocusable(false);
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setViewportView(componentsList);

        //rightPanel.add(componentInfoPanel);

        downPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Localization.getString("components_info")));
        downPanel.add(infoLabel);

        gridSnapButton.setToolTipText(Localization.getString("toggle_mouse_grid_snap_mode"));
        chipNameToggleButton.setToolTipText(Localization.getString("show_custom_real_chip_name"));
        gridToggleButton.setToolTipText(Localization.getString("show_hide_grid"));
        zoomInButton.setToolTipText(Localization.getString("zoom_in"));
        zoomOutButton.setToolTipText(Localization.getString("zoom_out"));
        zoomResetButton.setToolTipText(Localization.getString("reset_viewport_position"));
        showComponentInfoButton.setToolTipText(Localization.getString("show_component_info_window"));

        mainFrame = new JFrame("Circuit Simulator by George Tsakiridis");
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
        mainFrame.setJMenuBar(getJMenuBar());
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

        CSActionsManager.addAction(Localization.getString("new_project"), "proj_new", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), 'n', newProjectAction, projectMenu);
        CSActionsManager.addAction(Localization.getString("save_project"), "proj_save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), 's', saveProjectAction, projectMenu);
        CSActionsManager.addAction(Localization.getString("open_project"), "proj_load", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), 'o', loadProjectAction, projectMenu);
        CSActionsManager.addAction(Localization.getString("move_viewport"), "mf_viewport_move", KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), 0, getMouseFunctionChangeAction(MouseMode.CAMERA), null);
        CSActionsManager.addAction(Localization.getString("edit_text"), "mf_text", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), 0, getMouseFunctionChangeAction(MouseMode.TEXT), null);
        CSActionsManager.addAction(Localization.getString("toggle_component"), "mf_toggle", KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), 0, getMouseFunctionChangeAction(MouseMode.TOGGLE), null);
        CSActionsManager.addAction(Localization.getString("move_component"), "mf_move", KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), 0, getMouseFunctionChangeAction(MouseMode.MOVE), null);
        CSActionsManager.addAction(Localization.getString("add_component"), "mf_add", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), 0, getMouseFunctionChangeAction(MouseMode.ADD), null);
        CSActionsManager.addAction(Localization.getString("link_pins"), "mf_link", KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), 0, getMouseFunctionChangeAction(MouseMode.LINK), null);
        CSActionsManager.addAction(Localization.getString("remove_component"), "mf_remove", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), 0, getMouseFunctionChangeAction(MouseMode.REMOVE), null);
        CSActionsManager.addAction(Localization.getString("toggle_mouse_grid_snap_mode"), null, KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), 'G', mouseGridSnapModeAction, null);
        CSActionsManager.addAction(Localization.getString("start_simulation"), "sim_start", KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), 'S', getChangeSimulationAction(EmulationAction.START), simulationMenu);
        CSActionsManager.addAction(Localization.getString("stop_simulation"), "sim_stop", KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), 't', getChangeSimulationAction(EmulationAction.STOP), simulationMenu);
        CSActionsManager.addAction(Localization.getString("step_simulation"), "sim_step", KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), 'e', getChangeSimulationAction(EmulationAction.STEP), simulationMenu);
        CSActionsManager.addAction(Localization.getString("decrease_simulation_speed"), "sim_decrease_speed", KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), 'D', getSimulationSpeedSliderChangeAction(false), simulationMenu, true);
        CSActionsManager.addAction(Localization.getString("increase_simulation_speed"), "sim_increase_speed", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), 'I', getSimulationSpeedSliderChangeAction(true), simulationMenu);
        CSActionsManager.addAction(Localization.getString("lines_view_normal"), "view_line_normal", null, 'N', getChangeLineViewAction(LineViewMode.NORMAL), linesViewSubMenu);
        CSActionsManager.addAction(Localization.getString("lines_view_status"), "view_line_status", null, 'S', getChangeLineViewAction(LineViewMode.STATUS), linesViewSubMenu);
        CSActionsManager.addAction(Localization.getString("pins_view_normal"), "view_pin_normal", null, 'N', getChangePinViewAction(PinViewMode.NORMAL), pinsViewSubMenu);
        CSActionsManager.addAction(Localization.getString("pins_view_status"), "view_pin_status", null, 'S', getChangePinViewAction(PinViewMode.STATUS), pinsViewSubMenu);
        CSActionsManager.addAction(Localization.getString("pins_view_type"), "view_pin_type", null, 'T', getChangePinViewAction(PinViewMode.TYPE), pinsViewSubMenu);
        CSActionsManager.addAction(Localization.getString("show_custom_real_chip_name"), "view_chip_custom_name", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), 'C', chipNameToggleAction, viewMenu, true);
        CSActionsManager.addAction(Localization.getString("show_hide_grid"), "view_grid_toggle", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), 'G', gridToggleAction, viewMenu);
        CSActionsManager.addAction(Localization.getString("zoom_in"), "view_zoom_in", KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK), 'I', getZoomAction(1), viewMenu, true);
        CSActionsManager.addAction(Localization.getString("zoom_in")+2, null, KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), 0, getZoomAction(1), null);
        CSActionsManager.addAction(Localization.getString("zoom_out"), "view_zoom_out", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), 'O', getZoomAction(0), viewMenu);
        CSActionsManager.addAction(Localization.getString("zoom_out")+2, null, KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), 0, getZoomAction(0), null);
        CSActionsManager.addAction(Localization.getString("reset_viewport_position"), "view_zoom_reset", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), 'R', getZoomAction(2), viewMenu);
        CSActionsManager.addAction(Localization.getString("show_component_info_window"), "view_show_component_info", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK), 'n', showComponentInfoWindowAction, viewMenu, true);
        CSActionsManager.addAction(Localization.getString("settings"), "settings", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK), 'e', settingsAction, projectMenu, true);
        CSActionsManager.addAction(Localization.getString("help"), "help", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), 'H', helpAction, helpMenu);
        CSActionsManager.addAction(Localization.getString("about"), "about", null, 'A', aboutAction, helpMenu, true);

        CSActionsManager.bindAllTo(viewportPanel);

        helpMenu.addSeparator();
        JMenuItem versionMenuItem = new JMenuItem(Localization.getString("version") + ": " + ChipSimulator.PROGRAM_VERSION_STRING + " (" + Localization.getString("build") + " " + ChipSimulator.PROGRAM_VERSION + ")");
        versionMenuItem.setEnabled(false);
        versionMenuItem.setFocusable(false);
        versionMenuItem.setIcon(ResourceManager.getResource("build"));
        helpMenu.add(versionMenuItem);
    }

    private JMenuBar getJMenuBar(){
        JMenuBar menubar = new JMenuBar();

        projectMenu.setMnemonic('p');
        viewMenu.setMnemonic('v');
        simulationMenu.setMnemonic('s');
        helpMenu.setMnemonic('h');

        viewMenu.add(linesViewSubMenu);
        linesViewSubMenu.setMnemonic('l');
        linesViewSubMenu.setIcon(ResourceManager.getResource("view_line_normal"));
        viewMenu.add(pinsViewSubMenu);
        pinsViewSubMenu.setMnemonic('p');
        pinsViewSubMenu.setIcon(ResourceManager.getResource("view_pin_normal"));

        JMenu[] jMenus = new JMenu[]{projectMenu, viewMenu, simulationMenu, helpMenu};

        for(JMenu jMenu : jMenus){
            JMenu emptyMenu = new JMenu();
            emptyMenu.setFocusable(false);
            emptyMenu.setEnabled(false);

            menubar.add(jMenu);
            menubar.add(emptyMenu);
        }

        return menubar;
    }

    private boolean getValidation(){
        return JOptionPane.showConfirmDialog(null, Localization.getString("any_unsaved_progress_will_be_lost") +
                "\n" + Localization.getString("are_you_sure_you_want_to_continue")) == 0;
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

        lineViewChangeButtons.get(0).setEnabled(false);
        pinViewChangeButtons.get(0).setEnabled(false);

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
                for (Window.MouseModeChangeButton button : mouseModeChangeButtons) {
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

                for (Window.LineViewChangeButton button: lineViewChangeButtons) {
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

                for (Window.PinViewChangeButton button: pinViewChangeButtons) {
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

    private AbstractAction settingsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SettingsWindow(mainFrame);
        }
    };

    private AbstractAction helpAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                try {
                    Desktop.getDesktop().browse(URI.create("https://www.tsaky.com"));//TODO set correct link
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    private AbstractAction aboutAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AboutWindow(mainFrame);
        }
    };


    private class EmulationChangeButton extends ImageButton{
        private final EmulationAction emulationAction;

        public EmulationChangeButton(EmulationAction emulationAction, ImageIcon icon, String tooltip){
            super(icon);
            this.emulationAction = emulationAction;
            addActionListener(getChangeSimulationAction(emulationAction));
            setToolTipText(tooltip);
        }
    }

    private class LineViewChangeButton extends ImageButton{
        private final LineViewMode lineViewMode;

        public LineViewChangeButton(LineViewMode lineViewMode, ImageIcon icon, String tooltip){
            super(icon);
            this.lineViewMode = lineViewMode;
            addActionListener(getChangeLineViewAction(lineViewMode));
            setToolTipText(tooltip);
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
            setToolTipText(tooltip);
            addActionListener(getMouseFunctionChangeAction(mouseMode));
        }

    }

    private static class ImageButton extends JButton{
        public ImageButton(ImageIcon icon){
            setIcon(icon);
            setPreferredSize(new Dimension(30, 30));
        }
    }
}
