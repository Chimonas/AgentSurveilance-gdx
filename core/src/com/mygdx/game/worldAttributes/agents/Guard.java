package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.agents.ai.guard.GuardAI;
import com.mygdx.game.worldAttributes.agents.ai.guard.Stupid;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;

import java.util.ArrayList;

public class Guard extends Agent
{
    static final float VISIBILITY = 7.5f;

    public Guard(World world, Settings settings)
    {
        super(world, settings);
        visibility = VISIBILITY;
        ai = new Stupid();
    }

    @Override
    public void update()
    {
        if(active)
        {
            ((GuardAI) ai).update(velocity, angleFacing, getVisibleAreas(), getVisibleAgents());
            super.update();
        }
    }

    public ArrayList<Area> getVisibleAreas()
    {
        ArrayList<Area> visibleAreas = new ArrayList<>(); //add all visible areas and return

        return world.getMap().getAreaList();
    }
}
