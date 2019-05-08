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

        if(Math.random() < 0.2f / GameLoop.TICKRATE)
            intruder.createPheromone(Pheromone.PheromoneType.values()[(int)(Math.random() * Pheromone.PheromoneType.values().length)]);
    }

    public float getNewAngle()
    {
        return intruder.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float)GameLoop.TICKRATE;
    }

    public float getNewVelocity()
    {
        return intruder.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float)GameLoop.TICKRATE;
    }
}
