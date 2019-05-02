package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Target extends Area
{
    protected static final Color COLOR = Color.SALMON;
    protected static final float VISIBILITY = 0.0f;

    public Target(Vector2 topLeft, Vector2 bottomRight)
    {
        super(topLeft,bottomRight, COLOR, VISIBILITY);
    }
}
