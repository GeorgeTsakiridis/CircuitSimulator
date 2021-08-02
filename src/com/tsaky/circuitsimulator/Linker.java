package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.pin.Pin;
import com.tsaky.circuitsimulator.chip.pin.PinType;
import com.tsaky.circuitsimulator.ui.LineViewMode;
import com.tsaky.circuitsimulator.ui.ViewportPanel;

import java.awt.*;
import java.util.ArrayList;

public class Linker {

    private static final ArrayList<Pair> pairs = new ArrayList<>();

    public static void paint(Graphics g, int offsetX, int offsetY){

        for(Pair pair : pairs){
            Pin pin1 = pair.getPin1();
            Pin pin2 = pair.getPin2();

            Rectangle bounds1 = pin1.getBounds();
            Rectangle bounds2 = pin2.getBounds();

            Color c = g.getColor();
            if(ViewportPanel.lineViewMode == LineViewMode.NORMAL) {
                g.setColor(Color.BLACK);
                isLineHighForPin(pin1);
            }else if(ViewportPanel.lineViewMode == LineViewMode.STATUS){
                g.setColor(isLineHighForPin(pin1) ? Color.GREEN : Color.RED);
            }

            g.drawLine(bounds1.x + bounds1.width/2 + offsetX, bounds1.y + bounds1.height/2 + offsetY,
                    bounds2.x + bounds2.width/2 + offsetX, bounds2.y + bounds2.height/2 + offsetY);

            g.setColor(c);
        }
    }

    public static void linkPins(Pin pin1, Pin pin2, boolean removable){
        Pair newPair = new Pair(pin1, pin2, removable);

        for(Pair pair : pairs){
            if(pair.equals(newPair))return;
        }

        pairs.add(newPair);
    }

    public static void forceUnlinkPins(Pin pin1, Pin pin2){
        Pair pairToRemove = new Pair(pin1, pin2);

        for(Pair pair : pairs){
            if(pair.equals(pairToRemove)){
                pairToRemove = pair;
                break;
            }
        }

        pairs.remove(pairToRemove);
    }

    public static ArrayList<Pin> getAllConnectedPinsWith(Pin pin, ArrayList<Pin> connectedPins){
        if(connectedPins == null)connectedPins = new ArrayList<>();
        if(connectedPins.contains(pin))return connectedPins;

        connectedPins.add(pin);

        for(Pair pair : pairs){
            if(pair.contains(pin)){
                Pin otherPin = pair.getOtherPin(pin);
                connectedPins = getAllConnectedPinsWith(otherPin, connectedPins);
            }
        }

        return connectedPins;
    }

    public static void unlinkPin(Pin pin){
        ArrayList<Pair> pairsToRemove = new ArrayList<>();

        for(Pair pair : pairs){
            if(pair.contains(pin) && pair.isRemovable())pairsToRemove.add(pair);
        }

        for(Pair pair : pairsToRemove)pairs.remove(pair);
    }

    public static boolean checkForShorts(ArrayList<Pin> pins) {
        boolean first = true;
        boolean isHigh = false;

        boolean hasGroundSource = false;
        boolean hasVoltageSource = false;

        for (Pin pin : pins) {

            if (pin.getType() == PinType.OUTPUT) {
                if (first) {
                    first = false;
                    isHigh = pin.isHigh();
                } else {
                    if (isHigh != pin.isHigh()) {
                        //shortedPins.add(pinOutput);
                        Handler.SHORTED = true;
                        return true;
                    }
                }
            }
        }

        for (Pin pin : pins){
            if(pin.getType() == PinType.POWER_SOURCE){
                hasVoltageSource = true;
                if(hasGroundSource || (!first && !isHigh)){
                    Handler.SHORTED = true;
                    return true;
                }
            }
            else if(pin.getType() == PinType.GROUND_SOURCE){
                hasGroundSource = true;
                if(hasVoltageSource || (!first && isHigh)){
                    Handler.SHORTED = true;
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isLineHighForPin(Pin p) {
        ArrayList<Pin> pins = getAllConnectedPinsWith(p, null);
        //if(!Handler.EMULATION_RUNNING)return lastIsLineHigh;

        if(Linker.checkForShorts(pins))return true;

        for(Pin pin : pins){
            if(pin.getType() == PinType.POWER_SOURCE){
                return true;
            }
            else if(pin.getType() == PinType.GROUND_SOURCE){
                return false;
            }
            else if(pin.getType() == PinType.OUTPUT){
                return pin.isHigh();
            }
        }

        return false;
    }

    public static int getTotalPairs(){
        return pairs.size();
    }

    public static Pair getPair(int index){
        return pairs.get(index);
    }

    public static void clearPairs(){
        pairs.clear();
    }

}
