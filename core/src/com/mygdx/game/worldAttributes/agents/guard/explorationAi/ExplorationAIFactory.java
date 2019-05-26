package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.worldAttributes.agents.guard.Guard;

public class ExplorationAIFactory
{
    public static ExplorationAI newExplorationAI(ExplorationAI.ExplorationAIType explorationAIType, Guard guard)
    {
        switch(explorationAIType)
        {
            case STUPID:
                return new Stupid(guard);
//            case SWARM:
//                return new Swarm(guard);
            case HEURISTIC:
                return new HeuristicSearch(guard);
            default:
                return new Stupid(guard);

            //TODO: catch aitype not supported
        }
    }
}
