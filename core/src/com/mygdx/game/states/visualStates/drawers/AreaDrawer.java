package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.SentryTower;

public class AreaDrawer
{
    private static Color infillColor, borderColor, selectedColor;

    public static void render(ShapeRenderer shapeRenderer, Area area, boolean invalid, boolean selected)
    {
        infillColor = new Color(area.getColor());
        borderColor = new Color(Color.BLACK);
        selectedColor = new Color(Color.WHITE);

        if(invalid)
        {
            Gdx.gl.glEnable(GL20.GL_BLEND);

            infillColor.sub(0,0,0,0.5f);
            borderColor.sub(0,0,0,0.5f);
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(infillColor);
        shapeRenderer.rect(area.getTopLeft().x,area.getBottomRight().y,area.getWidth(),area.getHeight());
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if(selected)
            shapeRenderer.setColor(selectedColor);
        else
            shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(area.getTopLeft().x,area.getBottomRight().y,area.getWidth(),area.getHeight());

        shapeRenderer.end();

        if (area instanceof SentryTower)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(borderColor);
            shapeRenderer.circle(area.getTopLeft().x + SentryTower.WIDTH / 2, area.getTopLeft().y - SentryTower.WIDTH / 2, SentryTower.INNERRADIUS, 24);
            shapeRenderer.circle(area.getTopLeft().x + SentryTower.WIDTH / 2, area.getTopLeft().y - SentryTower.WIDTH / 2, SentryTower.OUTERRADIUS, 60);
            shapeRenderer.end();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
