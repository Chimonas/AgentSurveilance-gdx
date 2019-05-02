package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.math.Vector2;

public class AreaFactory
{
    private static Area.AreaType areaType;
    private static Area area;

    public static Area newArea(Vector2 startPoint, Vector2 endPoint)
    {
        switch (areaType)
        {
            case STRUCTURE:
                area = new Structure(startPoint, endPoint);
                break;
            case SENTRYTOWER:
                area = new SentryTower(endPoint);
                break;
            case SHADE:
                area = new Shade(startPoint, endPoint);
                break;
            case TARGET:
                area = new Target(startPoint,endPoint);
                break;
        }

        return area;
    }

    public static Area newArea(Vector2 startPoint)
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
