package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class GuardAI extends AI
{
    public enum GuardAIType
    {
        STUPID, SWARM_HEURISTIC;
    }

    protected ArrayList<Area> visibleAreas;

    public GuardAI(Guard guard)
    {
        super(guard);
    }

    public void update()
    {
        super.update();
        visibleAreas = ((Guard)agent).getVisibleAreas();
    }

    @Override
    public void spawn(Map map)
    {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        agent.spawnPosition(randomPosition, (float)Math.random() * 360.0f);
    }
}
