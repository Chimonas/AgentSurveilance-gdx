package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

public class AgentDrawer
{
    private static final Color
            GUARDCOLOR = Color.WHITE,
            INTRUDERCOLOR = Color.SCARLET,
            VISUALRANGECOLOR = new Color(1, 1, 1, 0.1f);
    private static final float AGENTSIZE = .5f;

    public static void render(ShapeRenderer shapeRenderer, Agent agent, boolean showVisualRange)
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

        if(showVisualRange)
        {
            float visualRange;

            if(agent instanceof Guard)
                visualRange = Intruder.VISIBILITY;
            else
                visualRange = Guard.VISIBILITY;

            visualRange *= agent.getVisualMultiplier();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(VISUALRANGECOLOR);
            shapeRenderer.arc(position.x, position.y, visualRange, agent.getAngleFacing() - agent.VISUAL_ANGLE / 2.0f, agent.VISUAL_ANGLE, 12);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

    }
}
