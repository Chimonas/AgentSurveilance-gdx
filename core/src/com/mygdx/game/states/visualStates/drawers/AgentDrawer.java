package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.Agent;
import com.mygdx.game.agents.Guard;

public class AgentDrawer
{
    private static final Color GUARDCOLOR = Color.ROYAL, INTRUDERCOLOR = Color.WHITE;
    private static final float AGENTSIZE = .5f;

    public static void render(ShapeRenderer shapeRenderer, Agent agent)
    {
        Vector2 position = agent.getPosition();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(agent instanceof Guard)
            shapeRenderer.setColor(GUARDCOLOR);
        else
            shapeRenderer.setColor(INTRUDERCOLOR);

        shapeRenderer.circle(position.x, position.y, AGENTSIZE, 12);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(position.x, position.y, AGENTSIZE, 12);
        shapeRenderer.end();
    }
}
