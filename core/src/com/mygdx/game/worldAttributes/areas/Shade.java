package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Shade extends Area
{
    protected static final Color COLOR = Color.OLIVE;
    protected static final float VISIBILITY = 12.0f;

    public Shade(Vector2 topLeft, Vector2 bottomRight)
    {
        super(topLeft,bottomRight, COLOR, VISIBILITY);
    }
}
