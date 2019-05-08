package com.mygdx.game.states.visualStates.drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.Sound;

public class SoundDrawer
{
    public static void render(ShapeRenderer shapeRenderer, Sound sound)
    {
        Vector2 position = sound.getPosition();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(position.x, position.y, sound.getVisibility(), 12 + 2 * (int)sound.getVisibility());
        shapeRenderer.end();
    }
}
