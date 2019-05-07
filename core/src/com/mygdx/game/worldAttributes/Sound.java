package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.math.Vector2;

public class Sound
{
    private Vector2 position;
    private float visibility;

    public Sound(Vector2 position, float visibility)
    {
        this.position = position;
        this.visibility = visibility;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public float getVisibility()
    {
        return visibility;
    }
}
