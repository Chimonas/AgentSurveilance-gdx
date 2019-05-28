package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.HeuristicBot;
import com.mygdx.game.worldAttributes.agents.PheromoneAI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class GuardAIFactory
{
    private static Agent agent;

    public static AI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard, ArrayList<Area> interalAreas)
    {
        agent = guard;
        switch(guardAIType) //New AIs should always take guard and optionally internalAreas
        {
            case STUPID:
                return new Stupid(guard);
            case PHEROMONE:
                return new HeuristicBot(agent);
            default:
                return new Stupid(guard);

            //TODO: catch aitype not supported
        }
    }

    public static AI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard)
    {
        return newGuardAI(guardAIType, guard, new ArrayList<Area>());
    }
}
