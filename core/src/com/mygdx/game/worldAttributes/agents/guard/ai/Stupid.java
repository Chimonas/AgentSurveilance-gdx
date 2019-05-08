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

        if(Math.random() < 0.2f / GameLoop.TICKRATE)
            guard.createPheromone(Pheromone.PheromoneType.values()[(int)(Math.random() * Pheromone.PheromoneType.values().length)]);
    }

    @Override
    public float getNewAngle()
    {
        return guard.getAngle() + 2.0f * ((float)Math.random() - 0.5f) * 30.0f;
    }

    @Override
    public float getNewVelocity()
    {
        return guard.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 1.0f;
    }
}
