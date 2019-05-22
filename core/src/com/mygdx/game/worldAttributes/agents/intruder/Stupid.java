package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;

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

        if(Math.random() < 0.1f / GameLoop.TICKRATE)
            agent.createPheromone(Pheromone.PheromoneType.values()[(int) (Math.random() * Pheromone.PheromoneType.values().length)]);

        newAngle = agent.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float) GameLoop.TICKRATE;
        newVelocity = agent.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float) GameLoop.TICKRATE;
    }
}
