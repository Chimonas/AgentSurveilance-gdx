package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.agents.AStarAI;

public class AStarGraphDrawer {

    public static void renderNodes(ShapeRenderer shapeRenderer, Vector2 aStarGraphNode)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(aStarGraphNode.x, aStarGraphNode.y, 0.2f);
        shapeRenderer.end();
    }

    public static void renderEdges(ShapeRenderer shapeRenderer, Vector2 firstNode, Vector2 secondNode)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(firstNode, secondNode);
        shapeRenderer.end();
    }

}
