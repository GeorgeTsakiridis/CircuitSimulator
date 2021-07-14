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
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Handler{

    private final long magicNumber = 1073147341387874804L;

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

    public void setViewMode(ViewMode viewMode){
        viewportPanel.setViewMode(viewMode);
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
            viewportPanel.resetOffsetAndScale();
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

    public boolean saveToFile(File file) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

        dataOutputStream.writeLong(magicNumber);
        dataOutputStream.writeInt(ChipSimulator.PROGRAM_VERSION);

        dataOutputStream.writeInt(viewportPanel.getOffsetX());
        dataOutputStream.writeInt(viewportPanel.getOffsetY());
        dataOutputStream.writeFloat(viewportPanel.getScale());
        dataOutputStream.writeInt(mouseMode.ordinal());
        dataOutputStream.writeInt(viewportPanel.getViewMode().ordinal());
        dataOutputStream.writeInt(emulationSpeed);

        dataOutputStream.writeInt(chipsOnScreen.size());
        for(Chip chip : chipsOnScreen){
            byte[] extraDataBytes = chip.getExtraDataBytes();

            dataOutputStream.writeUTF(chip.getChipName());
            dataOutputStream.writeInt(chip.getPosX());
            dataOutputStream.writeInt(chip.getPosY());
            dataOutputStream.writeInt(extraDataBytes == null ? 0 : extraDataBytes.length);
            if(extraDataBytes != null && extraDataBytes.length > 0) {
                dataOutputStream.write(extraDataBytes, 0, extraDataBytes.length);
            }
        }

        int totalPairs = Linker.getTotalPairs();

        dataOutputStream.writeInt(totalPairs);

        for(int i = 0; i < totalPairs; i++){
            Pair pair = Linker.getPair(i);
            int[] indexes1 = getChipIndexAndPinIndex(pair.getPin1());
            int[] indexes2 = getChipIndexAndPinIndex(pair.getPin2());

            if(indexes1[0] == -1 || indexes1[1] == -1 || indexes2[0] == -1 || indexes2[1] == -1){
                return false;
            }

            dataOutputStream.writeInt(indexes1[0]);
            dataOutputStream.writeInt(indexes1[1]);
            dataOutputStream.writeInt(indexes2[0]);
            dataOutputStream.writeInt(indexes2[1]);
        }

        return true;
    }

    public boolean loadFromFile(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

        reset();

        if(dataInputStream.readLong() != magicNumber){
            //not a valid file
            return false;
        }

        if(dataInputStream.readInt() != ChipSimulator.PROGRAM_VERSION){
            //not same version

        }

        int offsetX = dataInputStream.readInt();
        int offsetY = dataInputStream.readInt();

        viewportPanel.resetOffsetAndScale();
        viewportPanel.addOffset(offsetX, offsetY);
        viewportPanel.setScale(dataInputStream.readFloat());
        mouseMode = MouseMode.values()[dataInputStream.readInt()];
        setViewMode(ViewMode.values()[dataInputStream.readInt()]);
        emulationSpeed = dataInputStream.readInt();

        int totalChips = dataInputStream.readInt();
        for(int i = 0; i < totalChips; i++){
            String chipName = dataInputStream.readUTF();
            int posX = dataInputStream.readInt();
            int posY = dataInputStream.readInt();

            Chip chip = ChipManager.getNewChipInstance(chipName);
            chip.setPosition(posX, posY);
            chip.setExtraData(dataInputStream.readNBytes(dataInputStream.readInt()));
            chipsOnScreen.add(chip);
        }

        int totalPairs = dataInputStream.readInt();
        for(int i = 0; i < totalPairs; i++){
            int chipIndex1 = dataInputStream.readInt();
            int pinIndex1 = dataInputStream.readInt();
            int chipIndex2 = dataInputStream.readInt();
            int pinIndex2 = dataInputStream.readInt();

            Pin pin1 = chipsOnScreen.get(chipIndex1).getPin(pinIndex1);
            Pin pin2 = chipsOnScreen.get(chipIndex2).getPin(pinIndex2);

            Linker.linkPins(pin1, pin2);
        }

        viewportPanel.setChipsToPaint(chipsOnScreen);

        System.out.println(chipsOnScreen.size());

        return true;
    }

    public void reset(){
        Linker.clearPairs();
        mouseMode = MouseMode.CAMERA;
        setViewMode(ViewMode.NORMAL);
        selectedComponent = null;
        chipsOnScreen.clear();
        ChipUtils.unselectAllChips(chipsOnScreen);
        viewportPanel.setChipsToPaint(chipsOnScreen);
        lastSelectedPin = null;
        EMULATION_RUNNING = false;
        SHORTED = false;
        emulationMode = EmulationAction.STOP;
        emulationSpeed = 20;
    }

    private int[] getChipIndexAndPinIndex(Pin pin){
        int[] indexes = new int[]{-1, -1};

        for(int j = 0; j < chipsOnScreen.size(); j++){
            Pin[] pins = chipsOnScreen.get(j).getPins();

            for(int k = 0; k < pins.length; k++){
                if(pins[k] == pin){
                    indexes[0] = j;
                    indexes[1] = k;
                    return indexes;
                }
            }
        }

        return indexes;
    }

}
