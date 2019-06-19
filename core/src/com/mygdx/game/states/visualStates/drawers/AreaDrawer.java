package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Entrance;
import com.mygdx.game.worldAttributes.areas.SentryTower;
import com.mygdx.game.worldAttributes.areas.Structure;

public class AreaDrawer
{
    private static Color infillColor, borderColor, selectedColor, doorColor, windowColor;

    public static void render(ShapeRenderer shapeRenderer, Area area, boolean invalid, boolean selected)
    {
        infillColor = new Color(area.getColor());
        borderColor = new Color(Color.BLACK);
        selectedColor = new Color(Color.WHITE);
        doorColor = new Color(Color.YELLOW);
        windowColor = new Color(Color.SALMON);

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
        if(area instanceof Structure)
        {
            for(Entrance e: ((Structure)area).getDoors()){
                float width, height;
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(doorColor);
                if(e.getStartPosition().x == e.getEndPosition().x) {
                    width = 0.2f;
                    height = e.getEndPosition().y - e.getStartPosition().y;
                }else {
                    width = e.getEndPosition().x - e.getStartPosition().x;
                    height = 0.2f;
                }
                shapeRenderer.rect(e.getStartPosition().x, e.getStartPosition().y, width, height);
                shapeRenderer.end();
            }
            for(Entrance e: ((Structure)area).getWindows()){
                float width, height;
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(windowColor);
                if(e.getStartPosition().x == e.getEndPosition().x) {
                    width = 0.1f;
                    height = e.getEndPosition().y - e.getStartPosition().y;
                }else {
                    width = e.getEndPosition().x - e.getStartPosition().x;
                    height = 0.1f;
                }
                shapeRenderer.rect(e.getStartPosition().x, e.getStartPosition().y, width, height);
                shapeRenderer.end();
            }
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
