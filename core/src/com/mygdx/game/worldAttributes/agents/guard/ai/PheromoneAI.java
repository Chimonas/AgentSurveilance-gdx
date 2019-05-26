package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

public class PheromoneAI extends GuardAI
{
    public PheromoneAI(Guard guard)
    {
        super(guard);
    }

    @Override
    public void update()
    {
        super.update();

        //RED pheromone for seeing Intruders + maximize speed
        if(!visibleIntruders.isEmpty())
        {
            agent.createPheromone(Pheromone.PheromoneType.RED);
            newVelocity = agent.MAXVELOCITY;
        }
        else if(!visiblePheromones.isEmpty())
        {
            newVelocity = agent.MAXVELOCITY;
        }
        else
        {
            if(agent.getVelocity() == agent.MAXVELOCITY)
                newVelocity = 1.0f;
            else
                newVelocity = agent.getVelocity() + 0.05f / (float)GameLoop.TICKRATE;

            newAngle = agent.getAngleFacing() + 0.2f * agent.MAXTURNVELOCITY / (float)GameLoop.TICKRATE;
        }


        //PURPLE pheromone for being in a Shade
        if(agent.inShade()){
            agent.createPheromone(Pheromone.PheromoneType.PURPLE);
        }

        //

    }
}
