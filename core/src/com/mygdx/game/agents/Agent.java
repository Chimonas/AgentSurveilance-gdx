package com.mygdx.game.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.ai.AI;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

abstract public class Agent
{
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

    public Agent(Settings settings, float visibility)
    {
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

//    public void setInShade(boolean inShade)
//    {
//        this.inShade = inShade;
//
//        if(inShade)
//        {
//
//        }
//    }
//
//    public void changeVisualRangeInShadow(){
//        body.setVisualRange(body.getVisualRange()[0]/2,body.getVisualRange()[1]/2);
//        body.setVisualAngle(body.getVisualAngle()/2);
//    }
//
//    public void changeVisualAngleOutShadow(){
//        body.setVisualRange(body.getVisualRange()[0]*2, body.getVisualRange()[1]*2);
//        body.setVisualAngle(body.getVisualAngle()*2);
//    }

    public void update()
    {
        ai.update(velocity, angle);

        velocity = ai.getVelocity();
        angle = ai.getAngle();

        updatePosition();
    }

    private float velocityX, velocityY, angleRad;

    public void updatePosition()
    {
        angleRad = (float)Math.cos(Math.toRadians(angle));

        velocityX = velocity * (float)Math.cos(angleRad);
        velocityY = velocity * (float)Math.sin(angleRad);

        position.x += velocityX / GameLoop.TICKRATE;
        position.y += velocityY / GameLoop.TICKRATE;
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
