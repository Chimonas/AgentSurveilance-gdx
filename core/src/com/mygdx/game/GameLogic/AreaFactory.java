package com.mygdx.game.GameLogic;

import com.badlogic.gdx.math.Rectangle;


public class AreaFactory {

    static String typeOfAreaOnMouseClick;

    //There must be an abstract superclass called Area
    //This method should return objects of type Area
    //Each kind of area should contain
    // 1. a rectangle(the area to be drawn that represents the shape)
    // 2. a texture for that area
    public Rectangle addArea(int startX, int startY, int endX, int endY) {
        //sort coordinates so we can attain the correct numbers for
        //rectangle constructor Rectangle(topleftX, topleftY, width, height)
        int x,y,width,height;
        if (startY < endY) {
            y = startY;
        } else {
            y = endY;
        }
        if (startX < endX) {
            x = startX;
        } else {
            x = endX;
        }
        height = Math.abs(endY-startY);
        width = Math.abs(endX-startX);
        System.out.println("Rectangle with start coordinates x: " + x + "   y: " + y);
        System.out.println("Rectangle with height: " + height + "   and width: " + width);

        Rectangle rect = new Rectangle(x,y,width,height);

        return rect;
    }

}
