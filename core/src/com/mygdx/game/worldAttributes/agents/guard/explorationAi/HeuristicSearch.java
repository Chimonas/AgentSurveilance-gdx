package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;

public class HeuristicSearch extends GuardAI {
    public HeuristicSearch(Guard guard) {
        super(guard);
    }

    @Override
    public void update(){
        super.update();

        if(Math.random() < 0.1f / GameLoop.TICKRATE)
            guard.createPheromone(Pheromone.PheromoneType.values()[(int)(Math.random() * Pheromone.PheromoneType.values().length)]);
    }

    public float getNewAngle() {
        return 0;
    }

    public float getNewVelocity() {
        return 0;
    }
}
