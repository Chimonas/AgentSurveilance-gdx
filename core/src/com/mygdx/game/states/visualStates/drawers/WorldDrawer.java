package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

public class WorldDrawer
{
    public static void render(ShapeRenderer shapeRenderer, World world)
    {
        MapDrawer.render(shapeRenderer, world.getMap());

        for (Pheromone pheromone : world.getPheromones())
            PheromoneDrawer.render(shapeRenderer, pheromone);

        for (Guard guard : world.getGuards())
            if (guard.getActive())
                AgentDrawer.render(shapeRenderer, guard, true);

        for (Intruder intruder : world.getIntruders())
            if(intruder.getActive())
                AgentDrawer.render(shapeRenderer, intruder, true);

        for (Sound sound : world.getSounds())
            SoundDrawer.render(shapeRenderer, sound);
    }
}
