package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.Agent;
import com.mygdx.game.agents.Guard;

public class AgentDrawer
{
    private static final Color
            GUARDCOLOR = Color.ROYAL,
            INTRUDERCOLOR = Color.WHITE,
            VISUALRANGECOLOR = new Color(1, 0, 0, 0.5f);
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

        if(showVisualRange){
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(VISUALRANGECOLOR);
            shapeRenderer.arc(position.x, position.y, agent.getVisualRange()[0], agent.getAngleFacing(), agent.getVisualAngle());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    //    public void render(ShapeRenderer shapeRenderer, boolean showNoiseRadius, boolean showVisualRange){
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(position.x, position.y,(float)1);
//        shapeRenderer.end();
//
//        if(showNoiseRadius){
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle(position.x, position.y,(float)1 + noiseGeneratedRadius);
//            shapeRenderer.end();
//        }
//
//        if(showVisualRange){
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(new Color(1, 0, 0, 0.5f));
//            shapeRenderer.arc(position.x, position.y, visualRange[1], angleFacing, visualAngle);
//            shapeRenderer.end();
//            Gdx.gl.glDisable(GL20.GL_BLEND);
//        }
//    }

}
