package com.mygdx.game.agents.ai.intruder;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;

public class Stupid extends IntruderAI
{
    private Map map;
    private float velocity, angle;

    public Stupid(Map map)
    {
        this.map = map;
    }

    @Override
    public void update(float velocity, float angle)
    {
        this.velocity = velocity;
        this.angle = angle;
    }

    @Override
    public float getAngle()
    {
        return angle + 2.0f * ((float)Math.random() - 0.5f) * 720.0f / GameLoop.TICKRATE;
    }

    @Override
    public float getVelocity()
    {
        return velocity + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / GameLoop.TICKRATE;
    }
}
