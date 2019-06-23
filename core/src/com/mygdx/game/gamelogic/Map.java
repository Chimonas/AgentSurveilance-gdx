package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.worldAttributes.areas.*;

import java.util.ArrayList;

public class Map
{
    public static final float   MINWIDTH = 100.0f,
                                MINHEIGHT = 100.0f;
    public static final Color DEFAULTCOLOR = Color.FOREST;
    private float width, height;

    private ArrayList<SentryTower> sentryTowers;
    private ArrayList<Shade> shades;
    private ArrayList<Structure> structures;
    private ArrayList<Target> targets;

    public Map(float width, float height)
    {
        this.width = width < MINWIDTH ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;

        sentryTowers = new ArrayList<>();
        shades = new ArrayList<>();
        structures = new ArrayList<>();
        targets = new ArrayList<>();
    }

    public Map()
    {
        this(MINWIDTH, MINHEIGHT);
    }

    public void addArea(Area area)
    {
        if (area instanceof SentryTower)
        {
            sentryTowers.add((SentryTower)area);
        }
        else if (area instanceof Shade)
        {
            shades.add((Shade)area);
        }
        else if (area instanceof Structure)
        {
            structures.add((Structure)area);
        }
        else if (area instanceof Target)
        {
            targets.add((Target)area);
        }
    }

    public ArrayList<Area> getAreaList()
    {
        ArrayList<Area> areaList = new ArrayList<>();

        areaList.addAll(sentryTowers);
        areaList.addAll(shades);
        areaList.addAll(structures);
        areaList.addAll(targets);

        return areaList;
    }

    public ArrayList<SentryTower> getSentryTowers() {
        return sentryTowers;
    }

    public ArrayList<Shade> getShades() {
        return shades;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public void setSize(float width, float height)
    {
        this.width = (width < MINWIDTH) ? MINWIDTH : width;
        this.height = height < MINHEIGHT ? MINHEIGHT : height;

        for(SentryTower sentryTower: sentryTowers)
            if (!sentryTower.isInside(0, 0, width, height))
                sentryTowers.remove(sentryTower);

        for(Shade shade: shades)
            if (!shade.isInside(0, 0, width, height))
                shades.remove(shade);

        for(Structure structure: structures)
            if (!structure.isInside(0, 0, width, height))
                structures.remove(structure);

        for(Target target: targets)
            if (!target.isInside(0, 0, width, height))
                targets.remove(target);
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
