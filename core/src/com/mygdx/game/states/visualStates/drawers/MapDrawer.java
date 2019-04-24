package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.areas.Area;
import com.mygdx.game.gamelogic.Map;

public class MapDrawer
{
    /**
     * Draws the map background, possibly the grid, then all of the stored areas
     * @param shapeRenderer shapeRenderer to draw with
     * @param map map to draw
     * @param grid if to draw a grid
     * @param gridSize size of said grid
     */
    public static void render(ShapeRenderer shapeRenderer, Map map, boolean grid, float gridSize)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(1,-0.8f,map.getWidth(),map.getHeight());
        shapeRenderer.setColor(Map.DEFAULTCOLOR);
        shapeRenderer.rect(0,0,map.getWidth(), map.getHeight());
        shapeRenderer.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0,0,map.getWidth(),map.getHeight());

        if(grid)
        {
            shapeRenderer.setColor(new Color(0,0,0,0.5f));

            for(float i = gridSize; i < map.getWidth(); i+= gridSize)
                shapeRenderer.line(i, 0, i, map.getHeight());
            for(float i = gridSize; i < map.getHeight(); i+= gridSize)
                shapeRenderer.line(0,i,map.getWidth(),i);
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        for (Area area : map.getAreaList())
            AreaDrawer.render(shapeRenderer,area,false,false);
    }

    public static void render(ShapeRenderer shapeRenderer, Map map)
    {
        render(shapeRenderer,map,false,1f);
    }
}