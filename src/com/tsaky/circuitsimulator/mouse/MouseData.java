package com.tsaky.circuitsimulator.mouse;

import java.awt.*;

public class MouseData {
    public int x;
    public int y;
    public int xViewport;
    public int yViewport;
    public boolean isInsideViewport;

    public MouseData(int mouseX, int mouseY, Rectangle viewport){
        mouseX -= 8;
        mouseY -= 31;
        x = mouseX;
        y = mouseY;
        xViewport = mouseX - (int)viewport.getX();
        yViewport = mouseY - (int)viewport.getY();
        isInsideViewport = viewport.contains(mouseX, mouseY);
    }

    @Override
    public String toString() {
        return "MouseData{" +
                "x=" + x +
                ", y=" + y +
                ", xViewport=" + xViewport +
                ", yViewport=" + yViewport +
                ", isInsideViewport=" + isInsideViewport +
                '}';
    }
}
