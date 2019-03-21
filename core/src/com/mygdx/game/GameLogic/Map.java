package com.mygdx.game.GameLogic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Areas.Area;

import java.util.ArrayList;

public class Map
{
    private static final double MINWIDTH = 200,MINHEIGHT = 200;
    private double width,height;
    private ArrayList<Area> areaList;

    public Map(double width, double height)
    {
        this.width = width < MINWIDTH ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;
        areaList = new ArrayList<Area>();
    }

    public Map()
    {
        this(MINWIDTH, MINHEIGHT);
    }

    public void render(Batch batch)
    {
        for (Area area : areaList)
            area.render(batch);
    }

    public void addArea(Area area)
    {
        areaList.add(area);
    }

    public ArrayList<Area> getAreaList()
    {
        return areaList;
    }

    public void setSize(double width, double height)
    {
        this.width = width < MINWIDTH ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;

        for(Area area:areaList)
            if (!area.inBounds(0, 0, width, height))
                areaList.remove(area);
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }
}
