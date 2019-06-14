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

    public static AI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard, ArrayList<Area> internalAreas)
    {
        agent = guard;
        float [] coefficients = new float[] {
                    /* coefficient for sound: */ 0, //SOUND REALLY FUCKS UP GUARDS
                    /* coefficient for amount of visible guards: */ -17f,
                    /* coefficient for amount of visible intruders: */ 100.0f,
                    /* coefficient for red pheromones: */ 2f,
                    /* coefficient for green pheromones: */ 0,
                    /* coefficient for blue pheromones: */ 0,
                    /* coefficient for yellow pheromones: */ 0,
                    /* coefficient for purple pheromones: */ 0,
                    /* coefficient for map border: */ -1.5f,
                    /* coefficient for sentry towers: */ 0,
                    /* coefficient for shade: */ 0,
                    /* coefficient for structures: */ 0
        };


        switch(guardAIType) //New AIs should always take guard and optionally internalAreas
        {
            case STUPID:
                return new Stupid(guard);
            case SWARM_HEURISTIC:
                return new HeuristicBot(agent, coefficients);
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
