package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.Agent;

import java.util.ArrayList;

public abstract class IntruderAI extends AI
{
    public enum IntruderAIType
    {
        STUPID
    }

    protected Intruder intruder;
    protected ArrayList<Agent> visibleAgents;
    protected ArrayList<Sound> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    public IntruderAI(Intruder intruder)
    {
        this.intruder = intruder;
    }

    public void update()
    {
        visibleAgents = intruder.getVisibleAgents();
        visibleSounds = intruder.getVisibleSounds();
        visiblePheromones = intruder.getVisiblePheromones();
    }
}
