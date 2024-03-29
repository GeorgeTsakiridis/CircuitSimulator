package eu.gtsak.circuitsimulator.logic;

import eu.gtsak.circuitsimulator.chip.Chip;
import eu.gtsak.circuitsimulator.chip.ChipManager;
import eu.gtsak.circuitsimulator.chip.ChipNode;
import eu.gtsak.circuitsimulator.chip.ChipUtils;
import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.ui.Localization;
import eu.gtsak.circuitsimulator.ui.window.ComponentInfoPanel;
import eu.gtsak.circuitsimulator.ui.window.ViewportPanel;
import eu.gtsak.circuitsimulator.ui.window.Window;
import eu.gtsak.circuitsimulator.CircuitSimulator;
import eu.gtsak.circuitsimulator.EmulationAction;
import eu.gtsak.circuitsimulator.MouseMode;
import eu.gtsak.circuitsimulator.Pair;
import eu.gtsak.circuitsimulator.ui.LineViewMode;
import eu.gtsak.circuitsimulator.ui.PinViewMode;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Handler{

    private MouseMode mouseMode = MouseMode.CAMERA;
    private final Window window;
    private final ViewportPanel viewportPanel;
    private final ComponentInfoPanel componentInfoPanel;
    private Chip selectedComponent = null;
    private final ArrayList<Chip> chipsOnScreen = new ArrayList<>();
    private Pin lastSelectedPin = null;
    private EmulationAction emulationMode = EmulationAction.STOP;
    private int simulationSpeed = 500;
    private int lastRawX = 0;
    private int lastRawY = 0;

    public static boolean CALCULATING = false;
    public static boolean EMULATION_RUNNING = false;
    public static boolean SHORTED = false;

    public Handler() {
        viewportPanel = new ViewportPanel(this);
        componentInfoPanel = new ComponentInfoPanel();

        window = new Window(this, viewportPanel, componentInfoPanel);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!CALCULATING) viewportPanel.repaint();
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

    public void setSelectedComponent(String componentName){
        selectedComponent = ChipManager.getNewChipInstance(componentName);
        componentInfoPanel.setChip(selectedComponent);

        window.setChipDescription(selectedComponent);
        if(mouseMode == MouseMode.ADD){
            viewportPanel.updateGhostChip(selectedComponent);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, boolean leftClick) {
        //TEXT
        if(mouseMode == MouseMode.TEXT){
            ChipUtils.unselectAllChips(chipsOnScreen);
            Chip chip = ChipUtils.getChipBellowMouse(chipsOnScreen, mouseX, mouseY);

            if(chip != null) {

                JTextArea textArea = new JTextArea(chip.getDisplayName(), 5, 1);

                int option = JOptionPane.showConfirmDialog(null, textArea, Localization.getString("insert_new_display_name"),
                        JOptionPane.OK_CANCEL_OPTION);

                if(option == 2)return;

                String text = textArea.getText();
                if (text.length() > 0) {
                    chip.setDisplayName(text);
                }
            }
        }
        //ADD
        else if (mouseMode == MouseMode.ADD && selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)) {
            if (selectedComponent != null && !ChipUtils.chipCollidesWithOtherChip(selectedComponent, chipsOnScreen)) {
                Chip chip = selectedComponent.createNewInstance();
                chip.setPosition(selectedComponent.getPosX(), selectedComponent.getPosY());

                chipsOnScreen.add(chip);
                viewportPanel.setChipsToPaint(chipsOnScreen);
                chip.onAdded();
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
            if(!leftClick){
                pin = null;
                lastSelectedPin = null;
            }
            if (lastSelectedPin != null && pin == null) {
                Chip chip = new ChipNode();
                chip.setPosition(mouseX, mouseY);

                Linker.linkPins(lastSelectedPin, chip.getPin(0), true);

                chipsOnScreen.add(chip);
                viewportPanel.setChipsToPaint(chipsOnScreen);

                chip.onAdded();

                lastSelectedPin = chip.getPin(0);
                lastSelectedPin.setSelected(true);
            }
            if (pin != null) {
                if (lastSelectedPin == null) {
                    lastSelectedPin = pin;
                    pin.setSelected(true);
                } else {
                    if (lastSelectedPin != pin) {
                        Linker.linkPins(pin, lastSelectedPin, true);
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

    public void mousePressed(int rawMouseX, int rawMouseY){
        lastRawX = rawMouseX;
        lastRawY = rawMouseY;
    }

    public void mouseMoved(int mouseX, int mouseY){

        if(selectedComponent != null){
            selectedComponent.setPosition(mouseX, mouseY);
            if(viewportPanel.isMouseSnapEnabled()){
                selectedComponent.roundPosition();
            }
        }
    }

    public void mouseDragged(int mouseX, int mouseY, int rawMouseX, int rawMouseY){

        int difRawX = rawMouseX - lastRawX;
        int difRawY = rawMouseY - lastRawY;

        if(mouseMode == MouseMode.CAMERA) {
            viewportPanel.addOffset(difRawX, difRawY);
        }
        else if(mouseMode == MouseMode.MOVE){
                for (Chip chip : chipsOnScreen) {
                    if (chip.isSelected()) {
                        chip.setPosition(mouseX, mouseY);
                        if(viewportPanel.isMouseSnapEnabled()){
                            chip.roundPosition();
                        }
                    }
                }
        }

        lastRawX = rawMouseX;
        lastRawY = rawMouseY;
    }

    public void run() {
        while(true) {
            if (EMULATION_RUNNING) {
                CALCULATING = true;
                if(!ViewportPanel.PAINTING) {
                    for (Chip chip : chipsOnScreen) {
                        chip.calculate();
                    }
                }
                CALCULATING = false;
                if (emulationMode == EmulationAction.STEP) {
                    setEmulationMode(EmulationAction.STOP);
                }
            }
            try {
                //noinspection BusyWait
                Thread.sleep(simulationSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void reset(){
        mouseMode = MouseMode.CAMERA;
        ViewportPanel.lineViewMode = LineViewMode.NORMAL;
        ViewportPanel.pinViewMode = PinViewMode.NORMAL;

        emulationMode = EmulationAction.STOP;
        simulationSpeed = 500;

        Linker.clearPairs();
        ChipUtils.unselectAllChips(chipsOnScreen);
        ChipUtils.unselectAllPins(chipsOnScreen);
        for(Chip chip : chipsOnScreen)chip.onRemove();
        chipsOnScreen.clear();
        viewportPanel.setChipsToPaint(chipsOnScreen);
        viewportPanel.updateGhostChip(null);

        selectedComponent = null;
        lastSelectedPin = null;
        EMULATION_RUNNING = false;
        SHORTED = false;
        lastRawX = 0;
        lastRawY = 0;
        window.reset();
    }

    public void saveToFile(File file) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

        dataOutputStream.writeLong(CircuitSimulator.MAGIC_NUMBER_PROJ_SAVE);
        dataOutputStream.writeInt(CircuitSimulator.PROGRAM_VERSION);

        dataOutputStream.writeInt(viewportPanel.getOffsetX());
        dataOutputStream.writeInt(viewportPanel.getOffsetY());
        dataOutputStream.writeFloat(viewportPanel.getScale());

        dataOutputStream.writeInt(chipsOnScreen.size());
        for(Chip chip : chipsOnScreen){
            byte[] extraDataBytes = chip.getExtraDataBytes();

            dataOutputStream.writeUTF(chip.getSaveName());
            dataOutputStream.writeUTF(chip.getDisplayName());
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
            dataOutputStream.writeBoolean(pair.isRemovable());
        }
    }

    public void loadFromFile(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

        reset();

        Long fileMagicNumber = dataInputStream.readLong();

        if(!fileMagicNumber.equals(CircuitSimulator.MAGIC_NUMBER_PROJ_SAVE)){
            JOptionPane.showMessageDialog(null, Localization.getString("invalid_project"),
                    Localization.getString("failed_to_open_project"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(dataInputStream.readInt() != CircuitSimulator.PROGRAM_VERSION){
            JOptionPane.showMessageDialog(null,
                    Localization.getString("file_created_with_different_version"), Localization.getString("warning"), JOptionPane.WARNING_MESSAGE);
        }

        int offsetX = dataInputStream.readInt();
        int offsetY = dataInputStream.readInt();
        float scale = dataInputStream.readFloat();

        viewportPanel.resetOffsetAndScale();
        viewportPanel.addOffset(offsetX, offsetY);
        viewportPanel.setScale(scale);

        int totalChips = dataInputStream.readInt();
        for(int i = 0; i < totalChips; i++){
            String chipSaveName = dataInputStream.readUTF();
            String chipDisplayName = dataInputStream.readUTF();
            int posX = dataInputStream.readInt();
            int posY = dataInputStream.readInt();

            Chip chip = ChipManager.getNewChipInstance(chipSaveName);
            chip.setDisplayName(chipDisplayName);
            chip.setPosition(posX, posY);
            int chipDataLength = dataInputStream.readInt();
            byte[] chipData = new byte[chipDataLength];
            if(dataInputStream.read(chipData) != chipDataLength){
                JOptionPane.showMessageDialog(null, "",
                        Localization.getString("failed_to_open_project"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            chip.setExtraData(chipData);
            chipsOnScreen.add(chip);
        }

        int totalPairs = dataInputStream.readInt();
        for(int i = 0; i < totalPairs; i++){
            int chipIndex1 = dataInputStream.readInt();
            int pinIndex1 = dataInputStream.readInt();
            int chipIndex2 = dataInputStream.readInt();
            int pinIndex2 = dataInputStream.readInt();
            boolean removable = dataInputStream.readBoolean();

            Pin pin1 = chipsOnScreen.get(chipIndex1).getPin(pinIndex1);
            Pin pin2 = chipsOnScreen.get(chipIndex2).getPin(pinIndex2);

            Linker.linkPins(pin1, pin2, removable);
        }

        viewportPanel.setChipsToPaint(chipsOnScreen);
    }

}
