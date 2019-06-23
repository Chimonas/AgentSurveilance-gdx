package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.AStarAI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.AStarIntruderAI;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

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

        for(Communication communication : world.getCommunications())
            CommunicationDrawer.render(shapeRenderer, communication);

        for(Vector2 aStarGraphNode : world.getAStarGraphNodes())
            AStarGraphDrawer.renderNodes(shapeRenderer, aStarGraphNode);

        for(AStarAI.Pair<Vector2, Vector2> aStarGraphEdge : world.getAStarGraphEdges())
            AStarGraphDrawer.renderEdges(shapeRenderer, aStarGraphEdge.getLeft(), aStarGraphEdge.getRight());

    }
}
