package com.mygdx.game.worldAttributes.agents.intruder;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.HeuristicBot;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Target;

import java.util.ArrayList;

public class Intruder extends Agent
{
    public static final float VISIBILITY = 6.5f;
    public float enteredAreaTime = -1;
//    private boolean sprinting;

    public Intruder(World world)
    {
        super(world);
        visibility = VISIBILITY;

//        sprinting = false;
    }

    public void spawnRandomEdge(Map map)
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

        spawn(randomPosition, (float)Math.random() * 360.0f);
    }

    public void setIntruderAI(IntruderAI.IntruderAIType intruderAIType)
    {
        ai = IntruderAIFactory.newIntruderAI(intruderAIType, this);
    }

    public void setSimulationAI(GuardAI.GuardAIType guardAIType)
    {
        ArrayList<Area> internalAreas = new ArrayList<>();

        if(ai instanceof ExplorationAI)
            internalAreas = ((ExplorationAI) ai).getInternalAreas();

//        ai = GuardAIFactory.newGuardAI(guardAIType, this, internalAreas);
        ai = new HeuristicBot(this);
    }


//    public void setSprinting(boolean sprinting)
//    {
//        this.sprinting = sprinting;
//    }
}
