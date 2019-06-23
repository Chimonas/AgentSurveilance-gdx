package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;

public class InfoGainAlgo extends GuardAI {
    public InfoGainAlgo(Guard guard) {
        super(guard);
    }

    @Override
    public void update(){
        super.update();

        //if(Math.random() < 0.1f / GameLoop.TICK_RATE)
           // guard.createPheromone(Pheromone.PheromoneType.values()[(int)(Math.random() * Pheromone.PheromoneType.values().length)]);
    }
}