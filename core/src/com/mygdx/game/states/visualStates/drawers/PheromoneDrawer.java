package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.Pheromone;

public class PheromoneDrawer
{
    public static void render(ShapeRenderer shapeRenderer, Pheromone pheromone)
    {
        Vector2 position = pheromone.getPosition();
        Color color = new Color(pheromone.getPheromoneType().getColor());

        color.sub(0.0f,0.0f,0.0f,0.5f + 0.25f * (1.0f - pheromone.getIntensity()));

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);

        shapeRenderer.circle(position.x, position.y, pheromone.getVisibility(), 12 + 2 * (int)pheromone.getVisibility());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
