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
    public static float[] coefficients;

    public static AI newGuardAI(GuardAI.GuardAIType guardAIType, Guard guard, ArrayList<Area> internalAreas)
    {
        agent = guard;

        switch(guardAIType) //New AIs should always take guard and optionally internalAreas
        {
            case STUPID:
                return new Stupid(guard);
            case SWARM_HEURISTIC:
                //Default coefficients
                coefficients = new float[] {
                        /* coefficient for sound: */ 0, //SOUND REALLY FUCKS UP GUARDS
                        /* coefficient for amount of visible guards: */ -50f,
                        /* coefficient for amount of visible intruders: */ 200.0f,
                        /* coefficient for red pheromones: */ 10f,
                        /* coefficient for green pheromones: */ 5f,
                        /* coefficient for blue pheromones: */ 0f,
                        /* coefficient for yellow pheromones: */ 0,
                        /* coefficient for purple pheromones: */ 0,
                        /* coefficient for map border: */ 10.0f,
                        /* coefficient for sentry towers: */ 5.0f,
                        /* coefficient for shade: */ 1f,
                        /* coefficient for structures: */ 1.5f,
                        /* coefficient for inside structure: */ 20.0f
                };
                        //Combination of each/some variables added to this matrix
                        //Non-linear approach for optimization - > genetic learning, particle swarm optimazation

                return new HeuristicBot(agent, coefficients);
            case COMTEST:
                return new ComTestAI(guard);
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
