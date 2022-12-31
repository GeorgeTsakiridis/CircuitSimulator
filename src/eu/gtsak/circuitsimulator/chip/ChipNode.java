package eu.gtsak.circuitsimulator.chip;

import eu.gtsak.circuitsimulator.chip.pin.Pin;
import eu.gtsak.circuitsimulator.chip.pin.PinType;
import eu.gtsak.circuitsimulator.ui.Localization;

import java.awt.*;

public class ChipNode extends Chip {

    public ChipNode() {
        super("node", Localization.getString("node_name"),
                new Pin[]{
                        new Pin("Node", 0, PinType.NOT_USED),
                });

        setSizeWithoutPins(6, 6);
    }

    @Override
    public String getDescription() {
        return Localization.getString("node_description");
    }

    @Override
    public void calculate() {

    }

    @Override
    public void paintComponent(Graphics g, int offsetX, int offsetY, boolean realName, boolean pinDescription) {
        g.fillRect(getPosX()+offsetX-3, getPosY()+offsetY-3, 6, 6);

        getPin(0).setBounds(getPosX()-3, getPosY()-3, getWidth(), getHeight());
        getPin(0).paint(g, offsetX, offsetY, "", pinDescription, false);
    }

}
