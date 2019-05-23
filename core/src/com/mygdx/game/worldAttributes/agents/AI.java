package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

import java.util.ArrayList;

public abstract class AI
{
    protected Agent agent;
    protected ArrayList<Guard> visibleGuards;
    protected ArrayList<Intruder> visibleIntruders;
    protected ArrayList<Float> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    protected float newAngle, newVelocity;

    public void update()
    {
        visibleGuards = agent.getVisibleGuards();
        visibleIntruders = agent.getVisibleIntruders();
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
