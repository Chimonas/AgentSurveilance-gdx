package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

import java.util.ArrayList;

public class ComTestAI extends GuardAI
{
    Vector2 intruderPosition;

    public ComTestAI(Guard guard)
    {
        super(guard);

        newVelocity = agent.MAX_VELOCITY;
    }

    @Override
    public void update()
    {
        super.update();

        if(!visibleIntruders.isEmpty())
        {
            intruderPosition = visibleIntruders.get(0).getPosition();
            for(Guard guard : agent.getWorld().getGuards())
            {
                ArrayList<Vector2> message = new ArrayList();
                message.add(intruderPosition);
                agent.createCommunication(guard, message);
            }
        }
        else if(!receivedCommunications.isEmpty())
        {
            intruderPosition = (Vector2)receivedCommunications.get(0).getMessage().get(0);
        }
        else
        {
            intruderPosition = null;
        }

        if(intruderPosition != null)
        {
            newAngle = agent.getAngleBetweenTwoPos(agent.getPosition(), intruderPosition);
        }
        else
        {
            newAngle = agent.getAngleFacing() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float) GameLoop.TICK_RATE;
        }
    }
}
