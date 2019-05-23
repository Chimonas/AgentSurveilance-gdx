package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

import java.util.ArrayList;

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

        for(Communication communication : receivedCommunications)
        {
            System.out.println(communication.getMessage().size());
        }

        if(!visibleIntruders.isEmpty())
        {
            agent.createPheromone(Pheromone.PheromoneType.RED);
            ArrayList<Boolean> test = new ArrayList<>();
            test.add(true);
            agent.createCommunication(agent.getWorld().getGuards().get(0),test);
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
    }
}
