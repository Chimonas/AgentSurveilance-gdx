package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.agents.ai.AI;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Target;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;

import java.util.ArrayList;

abstract public class Agent
{
    protected World world;
    protected Settings settings;

    protected AI ai;
    protected float maxVelocity;
    protected float visualAngle;
    protected float visualMultiplier;
    protected float visibility;

    protected boolean active;
    protected Vector2 position;
    protected float angleFacing;
    protected float velocity;
    protected float turnVelocity;
    protected float turnAngle;
    protected float noiseGeneratedRadius;

//    protected boolean inShade;

    public Agent(World world, Settings settings)
    {
        this.world = world;
        this.settings = settings;

        maxVelocity = 1.4f;
        visualAngle = 45.0f;
        visualMultiplier = 1.0f;
        turnVelocity = 180.0f;
        setNoiseGeneratedRadius();

        active = false;
    }

    public void spawn(Vector2 position, float angle)
    {
        active = true;
        this.position = position;
        this.angleFacing = angle;
        velocity = 0f;
    }

    public void despawn()
    {
        active = false;
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
        velocity = ai.getNewVelocity();
        angleFacing = ai.getNewAngle();

        updatePosition();
    }

    private Vector2 newPosition;
    private float velocityX, velocityY, angleRad;

    public void updatePosition()
    {
        angleRad = (float)Math.cos(Math.toRadians(angleFacing));

        velocityX = velocity * (float)Math.cos(angleRad);
        velocityY = velocity * (float)Math.sin(angleRad);

        newPosition = new Vector2(position.x + velocityX / GameLoop.TICKRATE, position.y + velocityY / GameLoop.TICKRATE);

        if(isValidMove(position,newPosition))
            position.set(newPosition); // Maybe add close approach for when path intersects wall
    }

    public boolean isValidMove(Vector2 position, Vector2 newPosition)
    {
        if(Area.intersects(position, newPosition, new Vector2(0,world.getMap().getHeight()), new Vector2(world.getMap().getWidth(),world.getMap().getHeight()), new Vector2(0,0), new Vector2(world.getMap().getWidth(), 0)))
            return false;

        for (Area area : world.getMap().getAreaList())
            if (!(area instanceof Shade || area instanceof Target) && area.intersects(position, newPosition)) return false;

        return true;
    }

    public ArrayList<Agent> getVisibleAgents()
    {
        ArrayList<Agent> visibleAgents = new ArrayList<>();

        for(Agent agent : world.getAgents())
            if (agent != this && position.dst2(agent.position) < (agent.visibility * agent.visibility))
                visibleAgents.add(agent);

        return visibleAgents;
    }

    public boolean getActive()
    {
        return active;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public float getAngleFacing() { return angleFacing; }

    public void setAngleFacing(float angleFacing) {
        this.angleFacing = angleFacing;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVisualAngle() { return visualAngle;}

    public void setVisualAngle(float visualAngle) { this.visualAngle = visualAngle;}

    public float getTurnVelocity() { return turnVelocity; }

    public void setTurnVelocity(float turnVelocity) { this.turnVelocity = turnVelocity; }

    public float getNoiseGeneratedRadius(){ return noiseGeneratedRadius;};

    //To be called at every frame in order to update sound according to speed
    public void setNoiseGeneratedRadius() {

        if(velocity < 0.5){
            noiseGeneratedRadius = 1.0f;
            return;
        }
        else if(velocity < 1) {
            noiseGeneratedRadius = 3.0f;
            return;
        }
        else if(velocity < 2) {
            noiseGeneratedRadius = 5.0f;
            return;
        }
        else noiseGeneratedRadius = 10.0f;
    }


    //During a turn, update the angle facing every tick
    public float getTurnAngle(){return this.turnAngle; }

    public void setTurnAngle(float turnAngle){
        this.turnAngle = turnAngle;
    }

    public static void updateAngleFacing(ArrayList<Area> areas, Agent a, float newAngle){
        ArrayList<Agent> agentsInVision = new ArrayList<Agent>();

        float oldAngle = a.getAngleFacing();
        a.setTurnAngle(oldAngle - newAngle);
        a.setVelocity(0);
        a.setAngleFacing(oldAngle - (a.getTurnAngle()/a.getTurnVelocity()));
        a.setTurnAngle(newAngle - a.getAngleFacing());

    }
}
