package com.mygdx.game.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;

public abstract class Area {
    public enum AreaType {
        STRUCTURE, SENTRYTOWER, SHADE, TARGET
    }

    protected Vector2 topLeft, topRight, bottomLeft, bottomRight;
    protected Color color;
    protected float visibility, minWidth, minHeight;

    public Area(Vector2 topLeft, Vector2 bottomRight, Color color, float visibility, float minWidth, float minHeight)
    {
        this.topLeft = new Vector2();
        this.topRight = new Vector2();
        this.bottomLeft = new Vector2();
        this.bottomRight = new Vector2();

        setPosition(topLeft,bottomRight);

        this.color = color;
        this.visibility = visibility;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public Area(Vector2 topLeft, Vector2 bottomRight, Color color, float visibility)
    {
        this(topLeft, bottomRight, color, visibility, 1f, 1f);
    }

    public Area(float x, float y, float width, float height, Color defaultColor, float visibility) {
        this(new Vector2(x, y + height), new Vector2(x + width, y), defaultColor, visibility);
    }

    public float getWidth() {
        return bottomRight.x - topLeft.x;
    }

    public float getHeight() {
        return topLeft.y - bottomRight.y;
    }

    public void setPosition(Vector2 startPoint, Vector2 endPoint) {
        topLeft.x = startPoint.x < endPoint.x ? startPoint.x : endPoint.x;
        topLeft.y = startPoint.y > endPoint.y ? startPoint.y : endPoint.y;
        bottomRight.x = startPoint.x > endPoint.x ? startPoint.x : endPoint.x;
        bottomRight.y = startPoint.y < endPoint.y ? startPoint.y : endPoint.y;

        topRight = new Vector2(bottomRight.x, topLeft.y);
        bottomLeft = new Vector2(topLeft.x, bottomRight.y);
    }

    public boolean intersects(Area secondArea) {
        return !(topLeft.x >= secondArea.bottomRight.x || topLeft.y <= secondArea.bottomRight.y || bottomRight.x <= secondArea.topLeft.x || bottomRight.y >= secondArea.topLeft.y);
    }

    public boolean intersects(Vector2 position, Vector2 newPosition)
    {
        return intersects(position, newPosition, topLeft, topRight, bottomLeft, bottomRight);
    }

    public static boolean intersects(Vector2 position, Vector2 newPosition, Vector2 topLeft, Vector2 topRight, Vector2 bottomLeft, Vector2 bottomRight)
    {
        return intersects(position, newPosition, topLeft, topRight) || intersects(position, newPosition, topRight, bottomRight) || intersects(position, newPosition, bottomRight, bottomLeft) || intersects(position, newPosition, bottomLeft, topLeft);
    }

    public static boolean intersects(Vector2 position, Vector2 newPosition, Vector2 edgeStart, Vector2 edgeEnd)
    {
        float positionDx = newPosition.x - position.x;
        float positionDy = newPosition.y - position.y;
        float edgeDx = edgeEnd.x - edgeStart.x;
        float edgeDy = edgeEnd.y - edgeStart.y;
        float positionEdgeDotProduct = positionDx * edgeDy - positionDy * edgeDx;

        if (positionEdgeDotProduct == 0)
            return false;

        float cx = edgeStart.x - position.x;
        float cy = edgeStart.y - position.y;
        float t = (cx * edgeDy - cy * edgeDx) / positionEdgeDotProduct;

        if (t < 0 || t > 1) return false;

        float u = (cx * positionDy - cy * positionDx) / positionEdgeDotProduct;

        if (u < 0 || u > 1) return false;

        return true;
    }

    public boolean contains(Vector2 point) {
        return ((point.x < bottomRight.x) &&
                (point.x > topLeft.x) &&
                (point.y > topLeft.y) &&
                (point.y < bottomRight.y));
    }

    public boolean isInside(Vector2 topLeft, Vector2 bottomRight) {
        return (isInside(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y));
    }

    public boolean isInside(float left, float top, float right, float bottom) {
        return (topLeft.x >= left &&
                topLeft.y <= top &&
                bottomRight.x <= right &&
                bottomRight.y >= bottom);
    }

    public boolean isInside(Area area) {
        return (isInside(area.topLeft, area.bottomRight));
    }

    public boolean isInside(Map map) {
        return isInside(0, map.getHeight(), map.getWidth(), 0);
    }

    public boolean isValid(Map map)
    {
        if (getWidth() < minWidth || getHeight() < minHeight)
            return false;

        if (!isInside(map))
            return false;

        for (Area area : map.getAreaList()) {
            if (intersects(area))
                return false;
        }

        return true;
    }

    public Vector2 getTopLeft() {
        return topLeft;
    }

    public Vector2 getBottomRight() {
        return bottomRight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
