package com.mygdx.game.worldAttributes.agents.guard;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAIFactory;
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

    public void setExplorationAI(ExplorationAI.ExplorationAIType explorationAI)
    {
        ai = ExplorationAIFactory.newExplorationAI(explorationAI, this);
    }

    public void setSimulationAI(GuardAI.GuardAIType guardAIType)
    {
        ArrayList<Area> internalAreas = new ArrayList<>();

        if(ai instanceof ExplorationAI)
            internalAreas = ((ExplorationAI) ai).getInternalAreas();

        ai = GuardAIFactory.newGuardAI(guardAIType, this, internalAreas);
    }

    public ArrayList<Area> getVisibleAreas()
    {
        ArrayList<Area> visibleAreas = new ArrayList<>(); //add all visible areas and return

        return world.getMap().getAreaList();
    }

    public float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }

}
