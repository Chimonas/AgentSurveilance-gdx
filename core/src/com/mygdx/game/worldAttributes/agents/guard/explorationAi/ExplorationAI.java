package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class ExplorationAI extends GuardAI
{
    public enum ExplorationAIType
    {
        STUPID,SWARM,HEURISTIC
    }

    protected ArrayList<Area> internalAreas;

    public ExplorationAI(Guard guard)
    {
        super(guard);
    }

    public void update()
    {
        super.update();

        for(Area area : visibleAreas)
            addArea(area);
    }

    public void addArea(Area area)
    {
         if(!internalAreas.contains(area))
             internalAreas.add(area);
    }

    public ArrayList<Area> getInternalAreas()
    {
        return internalAreas;
    }
}
