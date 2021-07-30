package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.chip.ChipText;
import com.tsaky.circuitsimulator.chip.ChipUtils;
import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinOutput;
import com.tsaky.circuitsimulator.mouse.MouseMode;
import com.tsaky.circuitsimulator.ui.ViewMode;
import com.tsaky.circuitsimulator.ui.ViewportPanel;
import com.tsaky.circuitsimulator.ui.Window;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Handler{

    private MouseMode mouseMode = MouseMode.CAMERA;
    private final Window window;
    private final ViewportPanel viewportPanel;
    private Chip selectedComponent = null;
    private final ArrayList<Chip> chipsOnScreen = new ArrayList<>();
    private Pin lastSelectedPin = null;
    public static boolean EMULATION_RUNNING = false;
    public static boolean SHORTED = false;
    private EmulationAction emulationMode = EmulationAction.STOP;
    private int simulationSpeed = 500;
    private int lastX = 0;
    private int lastY = 0;

    public Handler() {
        viewportPanel = new ViewportPanel(this);
        window = new Window(this, viewportPanel);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewportPanel.repaint();
            }
        }, 0, 16); //Target for 60 fps


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

        if(mouseMode != MouseMode.CAMERA){
            ChipUtils.unselectAllChips(chipsOnScreen);
            ChipUtils.unselectAllPins(chipsOnScreen);
        }
    }

    public void setSimulationSpeed(int emulationSpeed){
        this.simulationSpeed = emulationSpeed;
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

    public void mouseClicked(int mouseX, int mouseY) {
        //TEXT
        if(mouseMode == MouseMode.TEXT){
            ChipUtils.unselectAllChips(chipsOnScreen);
            Chip chip = ChipUtils.getChipBellowMouse(chipsOnScreen, mouseX, mouseY);

            if(chip instanceof ChipText) {
                ChipText chipText = (ChipText) chip;

                JTextArea textArea = new JTextArea(chipText.getText(), 5, 1);

                int option = JOptionPane.showConfirmDialog(null, textArea, "Insert Text", JOptionPane.OK_CANCEL_OPTION);

                if(option == 2)return;

                String text = textArea.getText();
                if (text.length() > 0) {
                    chipText.setText(text);
                }
            }
        }
        //ADD
        else if (mouseMode == MouseMode.ADD && selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)) {
            if (selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)) {
                Chip chip = selectedComponent.createNewInstance();
                chip.setPosition(mouseX, mouseY);
                chipsOnScreen.add(chip);
                viewportPanel.setChipsToPaint(chipsOnScreen);
            }
        }
        //MOVE
        else if (mouseMode == MouseMode.MOVE) {
            ChipUtils.unselectAllChips(chipsOnScreen);
            ChipUtils.selectChipIfNotNull(ChipUtils.getChipBellowMouse(chipsOnScreen, mouseX, mouseY));
        }
        //LINK
        else if (mouseMode == MouseMode.LINK) {
            Pin pin = ChipUtils.getPinBellowMouse(chipsOnScreen, mouseX, mouseY);
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
            Chip chip = ChipUtils.getChipBellowMouse(chipsOnScreen, mouseX, mouseY);
            if (chip != null)chip.toggle();
        }
        //REMOVE
        else if (mouseMode == MouseMode.REMOVE) {
            Chip chip = ChipUtils.getChipBellowMouse(chipsOnScreen, mouseX, mouseY);
            if (chip != null) {
                ChipUtils.safelyRemoveChip(chipsOnScreen, chip);
            }
        }

    }

    public void mousePressed(int mouseX, int mouseY){
        lastX = mouseX;
        lastY = mouseY;
    }

    public void mouseReleased(int mouseX, int mouseY){

    }

    public void mouseMoved(int mouseX, int mouseY){
        if(selectedComponent != null){
            selectedComponent.setPosition(mouseX, mouseY);
        }
    }

    public void mouseDragged(int mouseX, int mouseY, int rawMouseX, int rawMouseY){

        if(mouseMode == MouseMode.CAMERA) {
            viewportPanel.addOffset(rawMouseX - lastX, rawMouseY - lastY);
            lastX = rawMouseX;
            lastY = rawMouseY;
        }
        else if(mouseMode == MouseMode.MOVE){
                for (Chip chip : chipsOnScreen) {
                    if (chip.isSelected()) {
                        chip.setPosition(mouseX, mouseY);
                    }
                }
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
                Thread.sleep(simulationSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset(){
        mouseMode = MouseMode.CAMERA;
        setViewMode(ViewMode.NORMAL);
        emulationMode = EmulationAction.STOP;
        simulationSpeed = 500;

        Linker.clearPairs();
        ChipUtils.unselectAllChips(chipsOnScreen);
        ChipUtils.unselectAllPins(chipsOnScreen);
        chipsOnScreen.clear();
        viewportPanel.setChipsToPaint(chipsOnScreen);

        selectedComponent = null;
        lastSelectedPin = null;
        EMULATION_RUNNING = false;
        SHORTED = false;
        lastX = 0;
        lastY = 0;
        window.reset();
    }

    public void saveToFile(File file) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

        dataOutputStream.writeLong(ChipSimulator.MAGIC_NUMBER);
        dataOutputStream.writeInt(ChipSimulator.PROGRAM_VERSION);

        dataOutputStream.writeInt(viewportPanel.getOffsetX());
        dataOutputStream.writeInt(viewportPanel.getOffsetY());
        dataOutputStream.writeFloat(viewportPanel.getScale());

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
            int[] indexes1 = ChipUtils.getChipIndexAndPinIndex(chipsOnScreen, pair.getPin1());
            int[] indexes2 = ChipUtils.getChipIndexAndPinIndex(chipsOnScreen, pair.getPin2());

            if(indexes1[0] == -1 || indexes1[1] == -1 || indexes2[0] == -1 || indexes2[1] == -1){
                return;
            }

            dataOutputStream.writeInt(indexes1[0]);
            dataOutputStream.writeInt(indexes1[1]);
            dataOutputStream.writeInt(indexes2[0]);
            dataOutputStream.writeInt(indexes2[1]);
        }
    }

    public void loadFromFile(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

        reset();

        Long fileMagicNumber = dataInputStream.readLong();

        if(!fileMagicNumber.equals(ChipSimulator.MAGIC_NUMBER)){
            JOptionPane.showMessageDialog(null, "This is not a valid project.",
                    "Failed to open project", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(dataInputStream.readInt() != ChipSimulator.PROGRAM_VERSION){
            JOptionPane.showMessageDialog(null,
                    "This save file has been created with a different program version.\n" +
                            "Project may not be loaded successfully.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        int offsetX = dataInputStream.readInt();
        int offsetY = dataInputStream.readInt();
        float scale = dataInputStream.readFloat();

        viewportPanel.resetOffsetAndScale();
        viewportPanel.addOffset(offsetX, offsetY);
        viewportPanel.setScale(scale);

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
    }

}
