package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SentryTower extends Area
{
    public static final float WIDTH = 2.0f, HEIGHT = 2.0f,INNERRADIUS = 2.0f, OUTERRADIUS = 15.0f, VISUAL_MULTIPLIER = 2.0f;
    protected static final Color COLOR = Color.ROYAL;
    protected static final float VISIBILITY = 24.0f;

    public SentryTower(Vector2 point)
    {
        super(point.x - WIDTH/2, point.y - HEIGHT / 2, WIDTH, HEIGHT, COLOR, VISIBILITY);
    }

    @Override
    public void setPosition(Vector2 startPoint, Vector2 endPoint)
    {
        super.setPosition(new Vector2(endPoint.x - WIDTH / 2.0f,endPoint.y + HEIGHT / 2.0f), new Vector2(endPoint.x + WIDTH / 2.0f,endPoint.y - HEIGHT / 2.0f));
    }
}
