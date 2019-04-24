package com.mygdx.game.agents.ai.guard;

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
        return angle + 2.0f * ((float)Math.random() - 0.5f) * 30.0f;
    }

    @Override
    public float getVelocity()
    {
        return velocity + 2.0f * ((float)Math.random() - 0.5f) * 1.0f;
    }
}
