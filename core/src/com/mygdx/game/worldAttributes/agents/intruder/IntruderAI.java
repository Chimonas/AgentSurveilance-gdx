package com.mygdx.game.worldAttributes.agents.intruder;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.AI;

public abstract class IntruderAI extends AI
{
    public enum IntruderAIType
    {
        STUPID
    }

    public IntruderAI(Intruder intruder)
    {
        agent = intruder;
    }

    @Override
    public void spawn(Map map)
    {
        float spawnPosition;
        Vector2 randomPosition = new Vector2();

        spawnPosition = (float)Math.random() * (2 * map.getHeight() + 2 * map.getWidth());

        if(spawnPosition <= map.getWidth())
        {
            randomPosition.x = spawnPosition;
            randomPosition.y = .1f;
        }
        else if(spawnPosition <= map.getWidth() + map.getHeight())
        {
            randomPosition.x = map.getWidth() - .1f;
            randomPosition.y = spawnPosition - map.getWidth();
        }
        else if(spawnPosition <= 2 * map.getWidth() + map.getHeight())
        {
            randomPosition.x = -spawnPosition + 2 * map.getWidth() + map.getHeight();
            randomPosition.y = map.getHeight() - .1f;
        }
        else
        {
            randomPosition.x = .1f;
            randomPosition.y = -spawnPosition + 2 * map.getWidth() + 2 * map.getHeight();
        }

        agent.spawn(randomPosition, (float)Math.random() * 360.0f);
    }

    public void setSprinting(boolean sprinting)
    {
        ((Intruder)agent).setSprinting(sprinting);
    }
}
