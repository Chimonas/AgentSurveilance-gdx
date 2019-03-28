package com.mygdx.game.GameLogic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.Areas.World;

import java.util.ArrayList;

public class Map
{
    private static final double MINWIDTH = 150,MINHEIGHT = 200;
    private double width,height;
    private ArrayList<Area> areaList;
    private World world;

    public Map(double width, double height)
    {
        this.width = width < MINWIDTH ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;
        areaList = new ArrayList<Area>();
        world = new World(width,height);
    }

    public Map()
    {
        this(MINWIDTH, MINHEIGHT);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        world.render(shapeRenderer);
        for (Area area : areaList)
            area.render(shapeRenderer);
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

    public World getWorld()
    {
        return world;
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
