package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.pin.Pin;

import java.awt.*;
import java.util.ArrayList;

public class Linker {

    private ArrayList<Link> links = new ArrayList<>();

    public void checkForEmptyLinks(){
        links.removeIf(link -> {
            if(link.size() <= 1){
                link.removeAll();
                return true;
            }
            return false;
        });
    }

    public void paint(Graphics g){

        for(Link link : links){
            int x = 0;
            int y = 0;
            int total = 0;
            for(Pin pin : link.getPins()){
                x += pin.getBounds().x + pin.getBounds().width/2;
                y += pin.getBounds().y + pin.getBounds().height/2;
                total++;
            }

            if(total == 0)return;

            x /= total;
            y /= total;
                Color c = g.getColor();
                g.setColor(link.c);
            for(Pin pin : link.getPins()){
                g.drawLine(x, y, pin.getBounds().x + pin.getBounds().width/2, pin.getBounds().y + pin.getBounds().height/2);
            }
            g.setColor(c);

        }

    }

    public void linkPins(Pin pin1, Pin pin2){
        Link link1 = pin1.getLink();
        Link link2 = pin2.getLink();
        boolean flag1 = link1 != null;
        boolean flag2 = link2 != null;

        if(!flag1 && !flag2){
            Link link = new Link(this);
            link.addPin(pin1);
            link.addPin(pin2);
            links.add(link);
        }
        else if(!flag1){
            link2.addPin(pin1);
        }
        else if(!flag2){
            link1.addPin(pin2);
        }else{
            mergeLinks(link1, link2);
        }
    }

    public void mergeLinks(Link link1, Link link2){
        for(Pin pin : link2.getPins()){
            link1.addPin(pin);
        }
        link2.removeAll();
        links.remove(link2);
    }

    public void unlinkPin(Pin pin){
        Link link = pin.getLink();

        if(link != null){
            link.removePin(pin);
        }
    }

}
