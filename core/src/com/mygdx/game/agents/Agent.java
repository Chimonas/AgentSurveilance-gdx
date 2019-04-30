package com.mygdx.game.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.ai.AI;
import com.mygdx.game.areas.Area;
import com.mygdx.game.areas.Shade;
import com.mygdx.game.areas.Target;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

abstract public class Agent
{
    protected Map map;
    protected Settings settings;

    protected AI ai;
    protected float maxVelocity;
    protected float visualAngle;
    protected float visibility;

    protected boolean active;
    protected Vector2 position;
    protected float angle;
    protected float velocity;

//    protected boolean inShade;

    public Agent(Map map, Settings settings, float visibility)
    {
        this.map = map;
        this.settings = settings;

        maxVelocity = 1.4f;
        visualAngle = 45.0f;
        this.visibility = visibility;

        active = false;
    }

    public void spawn(Vector2 position, float angle)
    {
        active = true;
        this.position = position;
        this.angle = angle;
        velocity = 0f;
    }

    public void spawnRandom(Map map)
    {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        spawn(randomPosition, 0.0f);
    }

    public void update()
    {
        if(active)
        {
            ai.update(velocity, angle);

            velocity = ai.getVelocity();
            angle = ai.getAngle();

            updatePosition();
        }
    }

    private Vector2 newPosition;
    private float velocityX, velocityY, angleRad;

    public void updatePosition()
    {
        angleRad = (float)Math.cos(Math.toRadians(angle));

        velocityX = velocity * (float)Math.cos(angleRad);
        velocityY = velocity * (float)Math.sin(angleRad);

        newPosition = new Vector2(position.x + velocityX / GameLoop.TICKRATE, position.y + velocityY / GameLoop.TICKRATE);

        if(isValidMove(position,newPosition))
            position.set(newPosition); // Maybe add close approach for when path intersects wall
    }

    public boolean isValidMove(Vector2 position, Vector2 newPosition)
    {
        if(Area.intersects(position, newPosition, new Vector2(0,map.getHeight()), new Vector2(map.getWidth(),map.getHeight()), new Vector2(0,0), new Vector2(map.getWidth(), 0)))
            return false;

        for (Area area : map.getAreaList())
            if (!(area instanceof Shade || area instanceof Target) && area.intersects(position, newPosition)) return false;

        return true;
    }


    public boolean getActive()
    {
        return active;
    }

    public Vector2 getPosition()
    {
        return position;
    }
}
