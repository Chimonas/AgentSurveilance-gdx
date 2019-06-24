package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.SentryTower;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Structure;

import java.util.ArrayList;

public abstract class GuardAI extends AI
{
    public enum GuardAIType
    {
        STUPID, SWARM_HEURISTIC,COMTEST;
    }

    protected ArrayList<Area> visibleAreas;
    protected ArrayList<SentryTower> visibleSentryTowers;
    protected ArrayList<Structure> visibleStructures;
    protected ArrayList<Shade> visibleShades;

    public GuardAI(Guard guard)
    {
        super(guard);
        this.agent = guard;
        visibleAreas = new ArrayList<>();
    }

    public void update()
    {
        super.update();
        visibleSentryTowers = ((Guard)agent).getVisibleSentryTowers();
        visibleStructures = ((Guard)agent).getVisibleStructures();
        visibleShades = ((Guard)agent).getVisibleShades();

        visibleAreas.addAll(visibleSentryTowers);
        visibleAreas.addAll(visibleStructures);
        visibleAreas.addAll(visibleShades);
    }

    @Override
    public void spawn(Map map)
    {

        Vector2 randomPosition = new Vector2();
        boolean isInside = false;
        do {
            randomPosition.x = (float) Math.random() * map.getWidth();
            randomPosition.y = (float) Math.random() * map.getHeight();

            isInside = false;
            for (Structure s : agent.getWorld().getMap().getStructures()) {
                if (s.contains(randomPosition)) {
                    isInside = true;
                    break;
                }
            }
        } while (isInside);

        agent.spawn(randomPosition, (float) Math.random() * 360.0f);

    }
}
