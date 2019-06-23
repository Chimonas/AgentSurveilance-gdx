package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class GuardAIFactory
{
    public static GuardAI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard, ArrayList<Area> interalAreas)
    {
        switch(guardAIType) //New AIs should always take guard and optionally internalAreas
        {
            case STUPID:
                return new Stupid(guard);
            case PHEROMONE:
                return new PheromoneAI(guard);
            case COMTEST:
                return new ComTestAI(guard);
            default:
                return new Stupid(guard);

            //TODO: catch aitype not supported
        }
    }

    public static GuardAI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard)
    {
        return newGuardAI(guardAIType, guard, new ArrayList<Area>());
    }
}
