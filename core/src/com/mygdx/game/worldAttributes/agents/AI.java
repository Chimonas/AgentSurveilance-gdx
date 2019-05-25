package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

import java.util.ArrayList;

public abstract class AI
{
    protected Agent agent;
    protected ArrayList<Guard> visibleGuards;
    protected ArrayList<Intruder> visibleIntruders;
    protected ArrayList<Communication> receivedCommunications;
    protected ArrayList<Float> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    protected float newAngle, newVelocity;

    public void update()
    {
        visibleGuards = agent.getVisibleGuards();
        System.out.println("");
        System.out.println("Position: " + agent.position.x + " " + agent.position.y);
        for(Guard g:  visibleGuards) System.out.println("Visible agents position: " + g.position.x + " " + g.position.y);
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
