package com.mygdx.game.worldAttributes.agents.guard;

import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAIFactory;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAIFactory;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Guard extends Agent
{
    public static final float VISIBILITY = 7.5f;

    public Guard(World world, Settings settings)
    {
        super(world, settings);
        visibility = VISIBILITY;

        if(settings.isExplorationPhase())
            ai = ExplorationAIFactory.newExplorationAI(settings.getExplorationAIType(), this);
        else
        {
            ai = GuardAIFactory.newGuardAI(settings.getGuardAIType(), this);
        }
    }

    public void setSimulationAI()
    {
        ArrayList<Area> internalAreas = ((ExplorationAI)ai).getInternalAreas();

        ai = GuardAIFactory.newGuardAI(settings.getGuardAIType(), this, internalAreas);
    }

    @Override
    public void update()
    {
        if(active)
        {
            ai.update();
            super.update();
        }
    }

    public ArrayList<Area> getVisibleAreas()
    {
        ArrayList<Area> visibleAreas = new ArrayList<>(); //add all visible areas and return

        return world.getMap().getAreaList();
    }
}
