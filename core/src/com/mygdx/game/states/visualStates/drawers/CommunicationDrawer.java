package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.worldAttributes.Communication;

public class CommunicationDrawer
{
    public static void render(ShapeRenderer shapeRenderer, Communication communication)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(communication.getSendingAgent().getPosition(), communication.getReceivingAgent().getPosition());
        shapeRenderer.end();
    }
}
