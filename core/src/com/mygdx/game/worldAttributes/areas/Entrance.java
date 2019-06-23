package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.math.Vector2;

public class Entrance
{
    private EntranceType type;
    private Vector2 startPosition, endPosition;

    private enum EntranceType
    {
        DOOR(5.0f),
        WINDOW(10.0f);

        private float soundDistance;

        EntranceType(float soundDistance)
        {
            this.soundDistance = soundDistance;
        }

        public float getSoundDistance()
        {
            return soundDistance;
        }
    }

    public Entrance(EntranceType type, Vector2 startPosition, Vector2 endPosition)
    {
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void setPosition(Vector2 startPoint, Vector2 endPoint)
    {
        this.startPosition = startPoint.cpy();
        this.endPosition = endPoint.cpy();
    }

    public float getSoundDistance()
    {
        return type.getSoundDistance();
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }
}

