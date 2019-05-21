package com.mygdx.game.agents.ai.guard;

import com.mygdx.game.gamelogic.GameLoop;

public class Stupid extends GuardAI
{
    private float velocity, angle;

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
    public float getVelocity() { return velocity + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / GameLoop.TICKRATE;}
}
