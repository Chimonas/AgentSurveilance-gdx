package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SentryTower extends Area
{
    private static final double WIDTH = 3, HEIGHT = 3,INNERRADIUS = 2, OUTERRADIUS = 15;

    public SentryTower(double[] point)
    {
        super(point[0] - WIDTH/2, point[1] - HEIGHT/2,WIDTH,HEIGHT, Color.ROYAL, Color.TEAL);
    }

    @Override
    public void setCoordinates(double[] firstPoint, double[] secondPoint)
    {
        topLeft = new double[]{secondPoint[0] - WIDTH/2,secondPoint[1] - HEIGHT/2};
        bottomRight = new double[]{secondPoint[0] + WIDTH/2,secondPoint[1] + HEIGHT/2};
    }

    @Override
    public void render(ShapeRenderer shapeRenderer)
    {
        super.render(shapeRenderer);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(lineColor);
        shapeRenderer.circle((float)(topLeft[0]+WIDTH/2), (float)(topLeft[1]+WIDTH/2),(float)INNERRADIUS);
        shapeRenderer.circle((float)(topLeft[0]+WIDTH/2), (float)(topLeft[1]+WIDTH/2),(float)OUTERRADIUS);
        shapeRenderer.end();
    }
}
