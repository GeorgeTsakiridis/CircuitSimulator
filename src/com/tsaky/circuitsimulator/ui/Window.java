package com.tsaky.circuitsimulator.ui;

import com.tsaky.circuitsimulator.EmulationAction;
import com.tsaky.circuitsimulator.Handler;
import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.mouse.MouseData;
import com.tsaky.circuitsimulator.mouse.MouseMode;
import com.tsaky.circuitsimulator.chip.ChipManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Window implements KeyListener, MouseListener, MouseMotionListener {

    private final Handler handler;
    private final ViewportPanel viewportPanel;

    private final JFrame mainFrame;

    private final JLabel infoLabel = new JLabel("Info Label");
    private final JLabel schematicLabel = new JLabel();
    private final JFrame pinoutFrame;

    private final JSlider simulationSpeedSlider;

    private final ImageButton newButton = new ImageButton("new.png");
    private final ImageButton saveButton = new ImageButton("save.png");
    private final ImageButton loadButton = new ImageButton("load.png");

    private final MouseModeChangeButton cameraButton = new MouseModeChangeButton(MouseMode.CAMERA, "cameraMove.png", "Move Camera");
    private final MouseModeChangeButton toggleButton = new MouseModeChangeButton(MouseMode.TOGGLE, "toggle.png", "Toggle Component");
    private final MouseModeChangeButton moveButton = new MouseModeChangeButton(MouseMode.MOVE, "move.png", "Move Component");
    private final MouseModeChangeButton addButton = new MouseModeChangeButton(MouseMode.ADD, "add.png", "Add Component");
    private final MouseModeChangeButton linkButton = new MouseModeChangeButton(MouseMode.LINK, "link.png", "Link Pins");
    private final MouseModeChangeButton removeButton = new MouseModeChangeButton(MouseMode.REMOVE, "remove.png", "Remove Component");
    private final ArrayList<MouseModeChangeButton> mouseModeChangeButtons = new ArrayList<>();

    private final EmulationChangeButton startEmulationButton = new EmulationChangeButton(EmulationAction.START, "emulationStart.png", "Start Emulation");
    private final EmulationChangeButton stopEmulationButton = new EmulationChangeButton(EmulationAction.STOP, "emulationStop.png", "Start Emulation");
    private final EmulationChangeButton stepEmulationButton = new EmulationChangeButton(EmulationAction.STEP, "emulationStep.png", "Step Emulation");
    private final ArrayList<EmulationChangeButton> emulationChangeButtons = new ArrayList<>();

    private final ViewChangeButton normalViewButton = new ViewChangeButton(ViewMode.NORMAL, "normalView.png", "Resume Normal View");
    private final ViewChangeButton lineViewButton = new ViewChangeButton(ViewMode.LINE_STATUS, "lineView.png", "View/Hide Lines Status");
    //private ViewChangeButton powerViewButton = new ViewChangeButton(ViewMode.POWER_STATUS, "powerView.png", "View/Hide Power Status");

    @SuppressWarnings("unchecked")
    public Window(Handler handler, ViewportPanel viewportPanel){
        this.handler = handler;
        this.viewportPanel = viewportPanel;

        normalViewButton.setEnabled(false);

        JList<String> componentsList = new JList<>(ChipManager.getAllChipNames());
        componentsList.setBackground(UIManager.getColor("background"));
        componentsList.addListSelectionListener(e -> handler.setSelectedComponent(((JList<String>) (e.getSource())).getSelectedValue()));

        //UP PANEL START
        simulationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 20, 1000, 20);
        simulationSpeedSlider.setPreferredSize(new Dimension(90, 20));
        simulationSpeedSlider.addChangeListener(e -> handler.setSimulationSpeed(((JSlider)e.getSource()).getValue()));
        simulationSpeedSlider.setToolTipText("Simulation Speed");

        JPanel upPanel = new JPanel();
        upPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Edit"));

        upPanel.add(newButton);
        upPanel.add(saveButton);
        upPanel.add(loadButton);
        upPanel.add(getNewSeperator());
        addMouseButtonsToPanel(upPanel);
        upPanel.add(getNewSeperator());
        addEmulationButtonsToPanel(upPanel);
        upPanel.add(simulationSpeedSlider);
        upPanel.add(getNewSeperator());
        addViewButtonsToPanel(upPanel);
        //UP PANEL END

        JScrollPane leftPanel = new JScrollPane();
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Components"));
        leftPanel.setFocusable(false);
        leftPanel.setPreferredSize(new Dimension(140, 0));
        leftPanel.setViewportView(componentsList);

        JPanel rightPanel = new JPanel();
        rightPanel.add(schematicLabel);

        JPanel downPanel = new JPanel();
        downPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Component Info"));
        downPanel.add(infoLabel);

        mainFrame = new JFrame("Circuit Simulator by George Tsakiridis");

        newButton.setToolTipText("New Project");
        saveButton.setToolTipText("Save Project");
        loadButton.setToolTipText("Open Project");

        newButton.addActionListener(e -> {
            handler.reset();
        });

        saveButton.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(mainFrame);
                handler.saveToFile(fileChooser.getSelectedFile());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        loadButton.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(mainFrame);
                handler.loadFromFile(fileChooser.getSelectedFile());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        mainFrame.setSize(680, 480);
        mainFrame.setMinimumSize(new Dimension(680, 400));
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.setFocusable(true);
        mainFrame.setFocusTraversalKeysEnabled(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(viewportPanel, "Center");
        mainFrame.add(leftPanel, "West");
        mainFrame.add(upPanel, "North");
        mainFrame.add(downPanel, "South");
        mainFrame.addKeyListener(this);
        mainFrame.addMouseListener(this);
        mainFrame.addMouseMotionListener(this);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        pinoutFrame = new JFrame("Component Pinout");
        pinoutFrame.add(rightPanel);
        pinoutFrame.setAlwaysOnTop(true);
        pinoutFrame.setSize(380, 400);
        pinoutFrame.setLocation(mainFrame.getLocationOnScreen().x + mainFrame.getSize().width, mainFrame.getLocationOnScreen().y );
        pinoutFrame.setVisible(true);
        pinoutFrame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSchematicImageSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        int w1 = mainFrame.getSize().width;
        int w2 = pinoutFrame.getSize().width;
        int space = 40;
        int x = Toolkit.getDefaultToolkit().getScreenSize().width/2 - (w1 + w2 + space)/2;
        int y = mainFrame.getLocationOnScreen().y;
        mainFrame.setLocation(x, y);
        pinoutFrame.setLocation(x + w1 + space, y);

    }

    private JSeparator getNewSeperator(){
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(3, 30));

        return separator;
    }

    private void addEmulationButtonsToPanel(JPanel panel){
        emulationChangeButtons.add(startEmulationButton);
        emulationChangeButtons.add(stepEmulationButton);
        emulationChangeButtons.add(stopEmulationButton);

        for(EmulationChangeButton button : emulationChangeButtons){
            panel.add(button);
        }
        stopEmulationButton.setEnabled(false);
    }

    private void addMouseButtonsToPanel(JPanel panel){
        mouseModeChangeButtons.add(cameraButton);
        mouseModeChangeButtons.add(toggleButton);
        mouseModeChangeButtons.add(moveButton);
        mouseModeChangeButtons.add(addButton);
        mouseModeChangeButtons.add(linkButton);
        mouseModeChangeButtons.add(removeButton);

        for(MouseModeChangeButton button : mouseModeChangeButtons){
            panel.add(button);
        }
        cameraButton.setEnabled(false);
    }

    private void addViewButtonsToPanel(JPanel panel){
        panel.add(normalViewButton);
        panel.add(lineViewButton);
        //panel.add(powerViewButton);
    }

    BufferedImage image = null;
    public void setInfoPage(InfoPage infoPage){
        infoLabel.setText(infoPage.getDescription());
        image = infoPage.getSchmematic();
        updateSchematicImageSize(pinoutFrame.getWidth(), pinoutFrame.getHeight());
    }

    private void updateSchematicImageSize(int width, int height){
        if(image != null){
            Dimension dSrc = new Dimension(image.getWidth(), image.getHeight());
            Dimension dDst = new Dimension(width, height-60);
            Dimension dimension = PaintUtils.getScaledDimension(dSrc, dDst);
            BufferedImage bufferedImage = PaintUtils.getScaledImage(image, dimension.width, dimension.height);
            schematicLabel.setIcon(bufferedImage != null ? new ImageIcon(bufferedImage) : null);
            schematicLabel.setText("");
        }else{
            schematicLabel.setIcon(null);
            schematicLabel.setText("Pinout not available for selected component");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        handler.pressedKey(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        handler.mouseClicked(generateMouseData(e.getX(), e.getY()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handler.mousePressed(generateMouseData(e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        handler.mouseReleased(generateMouseData(e.getX(), e.getY()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handler.mouseDragged(generateMouseData(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        handler.mouseMoved(generateMouseData(e.getX(), e.getY()));
    }

    private MouseData generateMouseData(int mouseX, int mouseY){
        Rectangle mainFrameBounds = mainFrame.getContentPane().getBounds();
        int posX = mouseX - mainFrameBounds.x;
        int posY = mouseY - viewportPanel.getY()/2;

        return new MouseData(posX, posY, viewportPanel.getBounds());
    }

    private void enableOtherMouseButtons(MouseMode mouseMode){
        for(MouseModeChangeButton mouseModeChangeButton : mouseModeChangeButtons){
            if(mouseModeChangeButton.mode != mouseMode){
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
        simulationSpeedSlider.setValue(20);
        normalViewButton.setEnabled(false);
        lineViewButton.setEnabled(true);
    }

    private class EmulationChangeButton extends ImageButton{
        private EmulationAction emulationAction;

        public EmulationChangeButton(EmulationAction emulationAction, String assetName, String tooltip){
            super(assetName);
            this.emulationAction = emulationAction;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
        }

        public void actionPerformed(ActionEvent e){
            handler.setEmulationMode(emulationAction);
        }

    }

    private class ViewChangeButton extends ImageButton{
        private ViewMode viewMode;

        public ViewChangeButton(ViewMode viewMode, String assetName, String tooltip){
            super(assetName);
            this.viewMode = viewMode;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
        }

        public void actionPerformed(ActionEvent e){
            handler.setViewMode(viewMode);
            this.setEnabled(false);
            if (this == normalViewButton) {
                lineViewButton.setEnabled(true);
            } else {
                normalViewButton.setEnabled(true);
            }
        }

    }

    private class MouseModeChangeButton extends ImageButton{
        private MouseMode mode;

        public MouseModeChangeButton(MouseMode mode, String assetName, String tooltip){
            super(assetName);
            this.mode = mode;
            addActionListener(this::actionPerformed);
            setToolTipText(tooltip);
        }

        public void actionPerformed(ActionEvent e) {
            handler.setMouseMode(mode);
            enableOtherMouseButtons(mode);
            ((JButton)(e.getSource())).setEnabled(false);
        }
    }

    private class ImageButton extends JButton{
        public ImageButton(String assetName){
            try {
                this.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/buttons/" + assetName))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(new Dimension(30, 30));
        }
    }

}
