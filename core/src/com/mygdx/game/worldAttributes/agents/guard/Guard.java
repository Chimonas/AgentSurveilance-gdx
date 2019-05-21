package com.mygdx.game.worldAttributes.agents.guard;

import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.HeuristicBot;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Guard extends Agent
{
    public static final float VISIBILITY = 7.5f;

    public Guard(World world, Settings settings)
    {
        super(world, settings);
        visibility = VISIBILITY;
//        ai = new Stupid(this);
        ai = new HeuristicBot(this);
        if(settings.isExplorationPhase())
        {
//            explorationAi = settings.getexplorationai
        }
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
