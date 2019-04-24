package com.mygdx.game.agents.ai;

public interface AI
{
    void update(float velocity, float angle);

    float getVelocity();
    float getAngle();
}
