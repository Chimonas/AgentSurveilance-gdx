package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.agents.Guard;
import com.mygdx.game.agents.Intruder;
import com.mygdx.game.gamelogic.World;

public class WorldDrawer
{
    public static void render(ShapeRenderer shapeRenderer, World world)
    {
        MapDrawer.render(shapeRenderer, world.getMap());

        for (Guard guard : world.getGuards())
            if (guard.getActive())
                AgentDrawer.render(shapeRenderer, guard);

        for (Intruder intruder : world.getIntruders())
            if(intruder.getActive())
                AgentDrawer.render(shapeRenderer, intruder);
    }
}
