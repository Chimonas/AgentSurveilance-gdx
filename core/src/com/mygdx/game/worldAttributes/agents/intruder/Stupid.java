package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.gamelogic.GameLoop;

public class Stupid extends IntruderAI
{
    public Stupid(Intruder intruder)
    {
        super(intruder);
    }

    @Override
    public void update()
    {
        super.update();

        if(Math.random() < 0.1f / GameLoop.TICK_RATE)
            setSprinting(true);
//            agent.createPheromone(Pheromone.PheromoneType.values()[(int) (Math.random() * Pheromone.PheromoneType.values().length)]);

        newAngle = agent.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float) GameLoop.TICK_RATE;
        newVelocity = agent.getMaxVelocity();
//        newVelocity = agent.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float) GameLoop.TICK_RATE;
    }
}
