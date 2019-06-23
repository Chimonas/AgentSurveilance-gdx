package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Structure extends Area
{
    protected static final Color COLOR = Color.SLATE;
    protected static final float VISIBILITY = 12.0f;

    private ArrayList<Entrance> doors = new ArrayList<>();
    private ArrayList<Entrance> windows = new ArrayList<>();

    public Structure(Vector2 topLeft, Vector2 bottomRight)
    {
        super(topLeft, bottomRight, COLOR, VISIBILITY);
    }

    public ArrayList<Entrance> getEntrances()
    {
        ArrayList<Entrance> entrances = new ArrayList<>();
        entrances.addAll(doors);
        entrances.addAll(windows);

        return entrances;
    }

    public ArrayList<Entrance> getDoors()
    {
        return doors;
    }

    public ArrayList<Entrance> getWindows()
    {
        return windows;
    }
}
