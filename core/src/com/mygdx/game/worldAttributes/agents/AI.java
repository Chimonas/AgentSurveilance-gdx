package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class AI
{
    protected Agent agent;
    protected ArrayList<Guard> visibleGuards;
    protected ArrayList<Intruder> visibleIntruders;
    protected ArrayList<Communication> receivedCommunications;
    protected ArrayList<Float> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;
    protected ArrayList<Area> visibleAreas;

    protected float newAngle, newVelocity;

    public AI(Agent agent) {
        this.agent = agent;
    }

    public void update()
    {
        if(agent instanceof Guard) visibleAreas = ((Guard)agent).getVisibleAreas();
        visibleGuards = agent.getVisibleGuards();
        visibleIntruders = agent.getVisibleIntruders();
        receivedCommunications = agent.getReceivedCommunications();
        visibleSounds = agent.getVisibleSounds();
        visiblePheromones = agent.getVisiblePheromones();
    }

    public float getNewAngle(float oldAngle)
    {
        return newAngle;
    }

    public float getNewVelocity()
    {
        return newVelocity;
    }
} 
