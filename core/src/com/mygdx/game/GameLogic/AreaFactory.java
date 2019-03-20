package com.mygdx.game.GameLogic;

import com.mygdx.game.Areas.Area;
import com.mygdx.game.Areas.Structure;

public class AreaFactory {

    public static final int STRUCTURE = 0;
    public static final int SENTRYTOWER = 1;
    public static final int VEGETATION = 2;
    public static final int TARGET = 3;

    private static int areaType;

    //There must be an abstract superclass called Area
    //This method should return objects of type Area
    //Each kind of area should contain
    // 1. a rectangle(the area to be drawn that represents the shape)
    // 2. a texture for that area
    public static Area addArea(int startX, int startY, int endX, int endY) {
        //sort coordinates so we can attain the correct numbers for
        //rectangle constructor Rectangle(topleftX, topleftY, width, height)

        switch (areaType)
        {
            case STRUCTURE:

                break;
            case SENTRYTOWER:

                break;
            case VEGETATION:

                break;
            case TARGET:

                break;
        }

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

        Area area = createArea(x,y,width,height);

        return area;
    }

    public static Area createArea(int x, int y, int width, int height)
    {
        Area area;

        area = new Structure(x,y,width,height);

        return area;
    }

    public static void setAreaType(int type)
    {
        areaType = type;
    }

}
