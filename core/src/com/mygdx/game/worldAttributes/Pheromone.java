package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;

import java.beans.Visibility;

public class Pheromone
{
    private static final float MAX_VISIBILITY = 5.0f, DURATION = 5.0f;

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
        YELLOW(Color.YELLOW),
        PURPLE(Color.PURPLE);

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
        intensity -= 1.0f / (DURATION * GameLoop.TICK_RATE);
        visibility += MAX_VISIBILITY / (DURATION * GameLoop.TICK_RATE);
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
