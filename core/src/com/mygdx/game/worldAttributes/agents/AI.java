package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.Pheromone;

import java.util.ArrayList;

public abstract class AI
{
    protected Agent agent;
    protected ArrayList<Agent> visibleAgents;
    protected ArrayList<Float> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    protected float newAngle, newVelocity;

    public void update()
    {
        visibleAgents = agent.getVisibleAgents();
        visibleSounds = agent.getVisibleSounds();
        visiblePheromones = agent.getVisiblePheromones();
    }

    public float getNewAngle()
    {
        return newAngle;
    }

    public float getNewVelocity()
    {
        return newVelocity;
    }
} 
