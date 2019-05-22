package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

public class Stupid extends ExplorationAI
{
    public Stupid(Guard guard)
    {
        super(guard);
    }

    float newAngle, newVelocity;

    @Override
    public void update()
    {
        super.update();

        for(Area area : visibleAreas)
            addArea(area);

        if(Math.random() < 0.1f / GameLoop.TICKRATE)
            guard.createPheromone(Pheromone.PheromoneType.values()[(int) (Math.random() * Pheromone.PheromoneType.values().length)]);

        newAngle = guard.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float) GameLoop.TICKRATE;
        newVelocity = guard.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float) GameLoop.TICKRATE;
    }

    public float getNewAngle()
    {
        return newAngle;
    }

    public float getNewVelocity()
    {
        return newVelocity;
    }
}