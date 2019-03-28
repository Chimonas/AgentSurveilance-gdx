package com.mygdx.game.Areas;

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
                area = new Vegetation(startPoint, endPoint);
                break;
            case TARGET:
                area = new Target(startPoint,endPoint);
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
