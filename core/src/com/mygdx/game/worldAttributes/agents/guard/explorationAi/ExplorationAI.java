package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class ExplorationAI implements AI
{
    enum AIType
    {
        SWARM
    }

    abstract void update(ArrayList<Area> areas);
}
