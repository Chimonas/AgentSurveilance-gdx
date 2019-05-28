package com.mygdx.game.worldAttributes.agents.guard;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.HeuristicBot;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAIFactory;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Guard extends Agent
{
    public static final float VISIBILITY = 7.5f;

    public Guard(World world)
    {
        super(world);
        visibility = VISIBILITY;
    }

    public void spawnRandom(Map map)
    {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        spawn(randomPosition, (float)Math.random() * 360.0f);
    }

    public void spawnLeft(Map map, float divideMap, int guardNumber) {
        Vector2 position = new Vector2();
        position.x = 0.0f;
        position.y = divideMap*guardNumber;

        spawn(position, 0.0f);
    }

    public void setExplorationAI(ExplorationAI.ExplorationAIType explorationAI)
    {
        ai = ExplorationAIFactory.newExplorationAI(explorationAI, this);
    }

    public void setSimulationAI(GuardAI.GuardAIType guardAIType)
    {
        ArrayList<Area> internalAreas = new ArrayList<>();

        if(ai instanceof ExplorationAI)
            internalAreas = ((ExplorationAI) ai).getInternalAreas();

//        ai = GuardAIFactory.newGuardAI(guardAIType, this, internalAreas);
        ai = new HeuristicBot(this);
    }

    public ArrayList<Area> getVisibleAreas()
    {
        ArrayList<Area> visibleAreas = new ArrayList<>(); //add all visible areas and return

        return world.getMap().getAreaList();
    }

    private float getAngle(Vector2 agent, Vector2 other)
    {
        return modulo((float)(Math.toDegrees(Math.atan2(agent.y - other.y, agent.x - other.x))),360.0f);
        //    return modulo((float)Math.toDegrees(Math.atan((agent.y - other.y)/(agent.x - other.x))), 360.0f); //agent.x - other.x could be 0 and fail
    }

    public float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }

}
