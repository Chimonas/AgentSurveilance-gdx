package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SentryTower extends Area
{
    private static final double WIDTH = 20, HEIGHT = 20,INNERRADIUS = 8, OUTERRADIUS = 60;

    public SentryTower(double[] point)
    {
        super(point[0] - WIDTH/2, point[1] - HEIGHT/2,WIDTH,HEIGHT, Color.BLUE, Color.TEAL);
    }

    @Override
    public void setCoordinates(double[] firstPoint, double[] secondPoint)
    {
        topLeft = new double[]{secondPoint[0] - WIDTH/2,secondPoint[1] - HEIGHT/2};
        bottomRight = new double[]{secondPoint[0] + WIDTH/2,secondPoint[1] + HEIGHT/2};
    }

    @Override
    public void render(Batch batch, ShapeRenderer shapeRenderer)
    {
        super.render(batch,shapeRenderer);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(lineColor);
        shapeRenderer.circle((float)(topLeft[0]+WIDTH/2), (float)(topLeft[1]+WIDTH/2),(float)INNERRADIUS);
        shapeRenderer.circle((float)(topLeft[0]+WIDTH/2), (float)(topLeft[1]+WIDTH/2),(float)OUTERRADIUS);
        shapeRenderer.end();

        batch.begin();
    }
}
