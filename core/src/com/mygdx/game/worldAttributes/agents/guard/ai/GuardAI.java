package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class GuardAI extends AI
{
    public enum GuardAIType
    {
        STUPID,PHEROMONE
    }

    protected ArrayList<Area> visibleAreas;

    public GuardAI(Guard guard)
    {
        super(guard);
    }

    public void update()
    {
        super.update();
        visibleAreas = ((Guard)agent).getVisibleAreas();
    }
}
