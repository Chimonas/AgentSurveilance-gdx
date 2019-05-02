package com.mygdx.game.worldAttributes.agents.ai.guard;

import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Stupid implements GuardAI
{
    private float velocity, angle;

    @Override
    public void update(float velocity, float angle, ArrayList<Area> areas, ArrayList<Agent> agents)
    {
        this.velocity = velocity;
        this.angle = angle;
    }

    @Override
    public float getNewAngle()
    {
        return angle + 2.0f * ((float)Math.random() - 0.5f) * 30.0f;
    }

    @Override
    public float getNewVelocity()
    {
        return velocity + 2.0f * ((float)Math.random() - 0.5f) * 1.0f;
    }
}
