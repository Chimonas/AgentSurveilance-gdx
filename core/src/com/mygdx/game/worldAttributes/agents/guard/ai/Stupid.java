package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

public class Stupid extends GuardAI
{
    public Stupid(Guard guard)
    {
        super(guard);
    }

    @Override
    public void update()
    {
        super.update();

        if(Math.random() < 0.1f / GameLoop.TICK_RATE)
            agent.createPheromone(Pheromone.PheromoneType.values()[(int) (Math.random() * Pheromone.PheromoneType.values().length)]);

        newAngle = agent.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float) GameLoop.TICK_RATE;
        newVelocity = agent.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float) GameLoop.TICK_RATE;
    }
}
