package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.HeuristicBot;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class IntruderAIFactory
{
    private static Agent agent;
    public static float[] coefficients; //sound, amount of visible guards, amount of visible intruders,
                                        //red ph, green ph, blue ph, yellow ph, purple ph, map border,
                                        //sentry towers, shade, structures

    public static AI newIntruderAI(IntruderAI.IntruderAIType intruderAIType, Intruder intruder, ArrayList<Area> areas)
    {
        agent = intruder;

        switch(intruderAIType) //New AIs should always take guard and optionally internalAreas
        {
            case STUPID:
                return new Stupid(intruder);
            case SWARM_HEURISTIC:
                coefficients = new float[]{
                        /* coefficient for sound: */ 0, //SOUND REALLY FUCKS UP GUARDS
                        /* coefficient for amount of visible guards: */ -100f,
                        /* coefficient for amount of visible intruders: */ -17.0f,
                        /* coefficient for red pheromones: */ -2f,
                        /* coefficient for green pheromones: */ 0,
                        /* coefficient for blue pheromones: */ 0,
                        /* coefficient for yellow pheromones: */ 0,
                        /* coefficient for purple pheromones: */ 0,
                        /* coefficient for map border: */ -1.0f,
                        /* coefficient for sentry towers: */ 0,
                        /* coefficient for shade: */ 0,
                        /* coefficient for structures: */ 0
                };
                return new HeuristicBot(agent, coefficients);
            default:
                return new Stupid(intruder);

            //TODO: catch aitype not supported
        }
    }

    public static AI newIntruderAI(IntruderAI.IntruderAIType intruderAIType, Intruder intruder)
    {
        return newIntruderAI(intruderAIType, intruder, new ArrayList<Area>());
    }

    public void setCoefficients(float[] coeff){
        this.coefficients = coeff;
    }
}
