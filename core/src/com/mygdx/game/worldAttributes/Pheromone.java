package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;

public class Pheromone
{
    public static final float GROWTHRATE = 0.5f, DECAYRATE = 0.2f;

    private PheromoneType pheromoneType;
    private Vector2 position;
    private float visibility, intensity;

    public Pheromone(PheromoneType pheromoneType, Vector2 position)
    {
        this.pheromoneType = pheromoneType;
        this.position = position;
        visibility = 0.0f;
        intensity = 1.0f;
    }

    public enum PheromoneType
    {
        RED(Color.RED),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE),
        YELLOW(Color.YELLOW);

        private Color color;

        PheromoneType(Color color)
        {
            this.color = color;
        }

        public Color getColor()
        {
            return color;
        }
    }

    public void update()
    {
        intensity -= DECAYRATE / GameLoop.TICKRATE;
        visibility += GROWTHRATE / GameLoop.TICKRATE;
    }

    public PheromoneType getPheromoneType() {
        return pheromoneType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getIntensity()
    {
        return intensity;
    }

    public float getVisibility()
    {
        return visibility;
    }
}
