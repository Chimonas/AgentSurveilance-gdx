package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Area
{
    public enum AreaType
    {
        STRUCTURE,SENTRYTOWER,VEGETATION,TARGET;
    }

    protected double[] topLeft, bottomRight;
    protected Color lineColor, infillColor;

    public Area(double[] topLeft, double[] bottomRight, Color lineColor,Color infillColor)
    {
        this.topLeft = new double[]{topLeft[0],topLeft[1]};
        this.bottomRight = new double[]{bottomRight[0],bottomRight[1]};
        this.lineColor = lineColor;
        this.infillColor = infillColor;
    }

    public Area(double x, double y, double width, double height, Color lineColor, Color infillColor)
    {
        this(new double[]{x,y}, new double[]{x + width, y + height}, lineColor, infillColor);
    }

    public double getWidth()
    {
        return bottomRight[0] - topLeft[0];
    }

    public double getHeight()
    {
        return bottomRight[1] - topLeft[1];
    }

    public void setCoordinates(double[] firstPoint, double[] secondPoint)
    {
        if(firstPoint[0]<secondPoint[0])
        {
            topLeft[0] = firstPoint[0];
            bottomRight[0] = secondPoint[0];
        }
        else
        {
            topLeft[0] = secondPoint[0];
            bottomRight[0] = firstPoint[0];
        }

        if(firstPoint[1]<secondPoint[1])
        {
            topLeft[1] = firstPoint[1];
            bottomRight[1] = secondPoint[1];
        }
        else
        {
            topLeft[1] = secondPoint[1];
            bottomRight[1] = firstPoint[1];
        }
    }

    public boolean intersects(Area secondArea)
    {
        return !(topLeft[0] >= secondArea.bottomRight[0] || topLeft[1] >= secondArea.bottomRight[1] || bottomRight[0] <= secondArea.topLeft[0] || bottomRight[1] <= secondArea.topLeft[1]);
    }

    public boolean inBounds(double[] topLeft, double[] bottomRight)
    {
        return (inBounds(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]));
    }

    public boolean inBounds(double top, double left, double bottom, double right)
    {
        return(this.topLeft[0] >= left && this.topLeft[1] >= top && this.bottomRight[0] <= right && this.bottomRight[1] <= bottom);
    }

    public void render(Batch batch)
    {
        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(infillColor);
        shapeRenderer.rect((float) topLeft[0], (float)topLeft[1],(float)this.getWidth(),(float)this.getHeight());
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(lineColor);
        shapeRenderer.rect((float) topLeft[0], (float)topLeft[1],(float)this.getWidth(),(float)this.getHeight());
        shapeRenderer.end();

        batch.begin();
    }
}
