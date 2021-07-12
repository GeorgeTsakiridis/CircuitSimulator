package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.chip.ChipUtils;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;
import com.tsaky.circuitsimulator.mouse.MouseData;
import com.tsaky.circuitsimulator.mouse.MouseMode;
import com.tsaky.circuitsimulator.ui.ViewMode;
import com.tsaky.circuitsimulator.ui.ViewportPanel;
import com.tsaky.circuitsimulator.ui.Window;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Handler{

    private MouseMode mouseMode = MouseMode.CAMERA;
    private Window window;
    private ViewportPanel viewportPanel;
    private Chip selectedComponent = null;
    private ArrayList<Chip> chipsOnScreen = new ArrayList<>();
    private Pin lastSelectedPin = null;
    public static boolean EMULATION_RUNNING = false;
    public static boolean SHORTED = false;
    private EmulationAction emulationMode = EmulationAction.STOP;
    private int emulationSpeed = 20;

    public Handler() {
        viewportPanel = new ViewportPanel();
        window = new Window(this, viewportPanel);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewportPanel.repaint();
            }
        }, 0, 16);


        Thread simulationThread = new Thread(this::run);
        simulationThread.start();

    }

    public void setMouseMode(MouseMode mouseMode){
        this.mouseMode = mouseMode;

        if(mouseMode != MouseMode.ADD){
            viewportPanel.updateGhostChip(null);
        }else{
            viewportPanel.updateGhostChip(selectedComponent);
        }
    }

    public void setEmulationSpeed(int emulationSpeed){
        this.emulationSpeed = emulationSpeed;
    }

    public void setEmulationMode(EmulationAction emulationMode){
        this.emulationMode = emulationMode;
        EMULATION_RUNNING = emulationMode != EmulationAction.STOP;
        window.enableOtherEmulationButtons(emulationMode);
    }

    public void toggleViewmode(ViewMode viewMode){
        System.out.println(viewMode);
    }

    public void setSelectedComponent(String componentName){
        selectedComponent = ChipManager.getNewChipInstance(componentName);
        window.setInfoPage(selectedComponent.getInfoPage());
        if(mouseMode == MouseMode.ADD){
            viewportPanel.updateGhostChip(selectedComponent);
        }
    }

    public void mouseClicked(MouseData mouse) {
        if (mouse.isInsideViewport) {
            //ADD
            if (mouseMode == MouseMode.ADD && selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)) {
                if(selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)){
                    Chip chip = selectedComponent.createNewInstance();
                    chip.setPosition(mouse.xViewport - viewportPanel.getOffsetX(), mouse.yViewport - viewportPanel.getOffsetY());
                    chipsOnScreen.add(chip);
                    viewportPanel.setChipsToPaint(chipsOnScreen);
                }
            }
            //MOVE
            else if (mouseMode == MouseMode.MOVE) {
                ChipUtils.unselectAllChips(chipsOnScreen);
                ChipUtils.selectChipIfNotNull(ChipUtils.getChipBellowMouse(chipsOnScreen, mouse.xViewport - viewportPanel.getOffsetX(), mouse.yViewport - viewportPanel.getOffsetY()));
            }
            //LINK
            else if (mouseMode == MouseMode.LINK) {
                Pin pin = ChipUtils.getPinBellowMouse(chipsOnScreen, mouse.xViewport, mouse.yViewport);
                ChipUtils.unselectAllPins(chipsOnScreen);

                if (lastSelectedPin != null && pin == null) {
                    lastSelectedPin = null;
                }
                if (pin != null) {
                    if (lastSelectedPin == null) {
                        lastSelectedPin = pin;
                        pin.setSelected(true);
                    } else {
                        if (lastSelectedPin != pin) {
                            Linker.linkPins(pin, lastSelectedPin);
                        } else {
                            Linker.unlinkPin(pin);
                        }
                        lastSelectedPin = null;
                    }
                }
            }
            //TOGGLE
            else if (mouseMode == MouseMode.TOGGLE) {
                Pin pin = ChipUtils.getPinBellowMouse(chipsOnScreen, mouse.xViewport, mouse.yViewport);
                if(pin instanceof PinOutput){
                    PinOutput pinOutput = (PinOutput)pin;
                    if(pinOutput.isToggleable())pinOutput.setHigh(!pinOutput.isHigh());
                }
            }
            //REMOVE
            else if(mouseMode == MouseMode.REMOVE){
                Chip chip = ChipUtils.getChipBellowMouse(chipsOnScreen, mouse.xViewport - viewportPanel.getOffsetX(), mouse.yViewport - viewportPanel.getOffsetY());
                if(chip != null){
                    ChipUtils.safelyRemoveChip(chipsOnScreen, chip);
                }
            }
        }
    }

    public void mousePressed(MouseData mouse){
        lastX = mouse.x;
        lastY = mouse.y;
    }

    public void mouseReleased(MouseData mouse){

    }

    public void mouseMoved(MouseData mouse){
        if(selectedComponent != null){
            selectedComponent.setPosition(mouse.xViewport - viewportPanel.getOffsetX(), mouse.yViewport - viewportPanel.getOffsetY());
        }
    }

    private int lastX = 0, lastY = 0;
    public void mouseDragged(MouseData mouse){
        if(mouseMode == MouseMode.CAMERA) {
            viewportPanel.addOffset(mouse.x - lastX, mouse.y - lastY);
        }
        else if(mouseMode == MouseMode.MOVE){
            if(selectedComponent != null) {
                for (Chip chip : chipsOnScreen) {
                    if (chip.isSelected()) {
                        chip.setPosition(chip.getPosX() + mouse.x-lastX, chip.getPosY() + mouse.y - lastY);
                    }
                }
            }
        }
        lastX = mouse.x;
        lastY = mouse.y;

    }

    public void pressedKey(int keyCode){
        if(keyCode == KeyEvent.VK_LEFT){
            viewportPanel.addOffset(5, 0);
        }
        else if(keyCode == KeyEvent.VK_RIGHT){
            viewportPanel.addOffset(-5, 0);
        }
        else if(keyCode == KeyEvent.VK_UP){
            viewportPanel.addOffset(0, 5);
        }
        else if(keyCode == KeyEvent.VK_DOWN){
            viewportPanel.addOffset(0, -5);
        }
        else if(keyCode == KeyEvent.VK_R){
            viewportPanel.resetOffset();
        }
        else if(keyCode == KeyEvent.VK_Q){
            viewportPanel.increaseScale();
        }
        else if(keyCode == KeyEvent.VK_W){
            viewportPanel.decreaseScale();
        }
        else if(keyCode == KeyEvent.VK_A){
            viewportPanel.repaint();
        }
        else if(keyCode == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
    }

    public void run() {
        while(true) {
            if (EMULATION_RUNNING) {
                //linker.checkForEmptyLinks(); //TODO remove the line. Left for reference
                for (Chip chip : chipsOnScreen) {
                    chip.calculateOutputs();
                }
                if (emulationMode == EmulationAction.STEP) {
                    setEmulationMode(EmulationAction.STOP);
                }
            }
            try {
                Thread.sleep(emulationSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
