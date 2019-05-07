package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.Pheromone;

public class PheromoneDrawer
{
    public static void render(ShapeRenderer shapeRenderer, Pheromone pheromone)
    {
        Vector2 position = pheromone.getPosition();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(pheromone.getPheromoneType().getColor());

        shapeRenderer.circle(position.x, position.y, pheromone.getVisibility(), 12);
        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.BLACK);
//        shapeRenderer.circle(position.x, position.y, pheromone.getVisibility(), 12);
//        shapeRenderer.end();
    }
}
