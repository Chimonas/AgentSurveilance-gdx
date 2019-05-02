package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;

public class Pheromone
{
    public static final float GROWTHRATE = 0.5f, DECAYRATE = 0.2f;

    private PheromoneType pheromoneType;
    private Vector2 position;
    private float size, intensity;

    public Pheromone(PheromoneType pheromoneType, Vector2 position)
    {
        this.pheromoneType = pheromoneType;
        this.position = position;
        size = 0.0f;
        intensity = 1.0f;
    }

    private enum PheromoneType
    {
        RED, GREEN, BLUE, YELLOW
    }

    public void update()
    {
        intensity -= DECAYRATE / GameLoop.TICKRATE;
        size += GROWTHRATE / GameLoop.TICKRATE;
    }

    public float getIntensity()
    {
        return intensity;
    }
}
