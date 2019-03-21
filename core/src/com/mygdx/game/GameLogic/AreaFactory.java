package com.mygdx.game.GameLogic;

import com.mygdx.game.Areas.Area;
import com.mygdx.game.Areas.SentryTower;
import com.mygdx.game.Areas.Structure;
import com.mygdx.game.Areas.Target;

public class AreaFactory {

    private static Area.AreaType areaType;
    private static Area area;

    public static Area newArea(double[] startPoint, double[] endPoint)
    {
        switch (areaType)
        {
            case STRUCTURE:
                area = new Structure(startPoint, endPoint);
                break;
            case SENTRYTOWER:
                area = new SentryTower(endPoint);
                break;
            case VEGETATION:
                area = new Target(startPoint, endPoint);
                break;
            case TARGET:

                break;
            default:
                break;
        }

        return area;
    }

    public static Area newArea(double[] startPoint)
    {
        return newArea(startPoint,startPoint);
    }

    public static void setAreaType(Area.AreaType areaType)
    {
        AreaFactory.areaType = areaType;
    }

    public static Area.AreaType getAreaType()
    {
        return areaType;
    }

}
