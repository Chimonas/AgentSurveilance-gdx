package com.mygdx.game.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SentryTower extends Area
{
    public static final float WIDTH = 2, HEIGHT = 2,INNERRADIUS = 2, OUTERRADIUS = 15;
    protected static final Color COLOR = Color.ROYAL;

    public SentryTower(Vector2 point)
    {
        super(point.x - WIDTH/2, point.y - HEIGHT / 2, WIDTH, HEIGHT, COLOR);
    }

    @Override
    public void setPosition(Vector2 startPoint, Vector2 endPoint)
    {
        topLeft = new Vector2(endPoint.x - WIDTH / 2,endPoint.y + HEIGHT / 2);
        bottomRight = new Vector2(endPoint.x + WIDTH / 2,endPoint.y - HEIGHT / 2);
    }
}
