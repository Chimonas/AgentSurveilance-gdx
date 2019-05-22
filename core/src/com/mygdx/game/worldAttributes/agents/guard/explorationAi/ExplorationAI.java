package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class ExplorationAI extends AI
{
    public enum ExplorationAIType
    {
        STUPID,SWARM
    }

    protected Guard guard;
    protected ArrayList<Area> internalAreas;

    protected ArrayList<Area> visibleAreas;
    protected ArrayList<Agent> visibleAgents;
    protected ArrayList<Sound> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    public ExplorationAI(Guard guard)
    {
        this.guard = guard;
    }

    public void update()
    {
        visibleAreas = guard.getVisibleAreas();
        visibleAgents = guard.getVisibleAgents();
        visibleSounds = guard.getVisibleSounds();
        visiblePheromones = guard.getVisiblePheromones();
    }

    public void addArea(Area area)
    {
        boolean exists = false;

        for(Area existingArea : internalAreas)
            if (existingArea == area)
            {
                exists = true;
                break;
            }

        if(!exists)
            internalAreas.add(area);
    }

    public ArrayList<Area> getInternalAreas()
    {
        return internalAreas;
    }
}
