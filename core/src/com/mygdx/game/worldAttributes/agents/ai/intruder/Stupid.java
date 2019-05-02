package com.mygdx.game.worldAttributes.agents.ai.intruder;

import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.gamelogic.Map;

import java.util.ArrayList;

public class Stupid implements IntruderAI
{
    private Map map;
    private float velocity, angle;
    private ArrayList<Agent> agents;

    public Stupid(Map map)
    {
        this.map = map;
    }

    @Override
    public void update(float velocity, float angle, ArrayList<Agent> agents)
    {
        this.velocity = velocity;
        this.angle = angle;
        this.agents = new ArrayList<>(agents);
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
