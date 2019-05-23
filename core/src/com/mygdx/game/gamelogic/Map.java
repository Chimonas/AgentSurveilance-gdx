package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Map
{
    public static final float   MINWIDTH = 50.0f,
                                MINHEIGHT = 50.0f;
    public static final Color DEFAULTCOLOR = Color.FOREST;
    private float width, height;
    private ArrayList<Area> areaList;

    public Map(float width, float height)
    {
        this.width = width < MINWIDTH ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;
        areaList = new ArrayList<>();
    }

    public Map()
    {
        this(MINWIDTH, MINHEIGHT);
    }

    public void addArea(Area area)
    {
            areaList.add(area);
    }

    public ArrayList<Area> getAreaList()
    {
        return areaList;
    }

    public void setSize(float width, float height)
    {
        this.width = (width < MINWIDTH) ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;

        for(Area area:areaList)
            if (!area.isInside(0, 0, width, height))
                areaList.remove(area);
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }
}
