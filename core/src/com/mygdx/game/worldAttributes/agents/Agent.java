package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Target;

import java.util.ArrayList;
import java.util.Random;

abstract public class Agent
{
    protected World world;
    protected Settings settings;

    public final float VISUALANGLE = 45.0f, MAXVELOCITY = 1.4f, MAXTURNVELOCITY = 180.0f, PHEROMONECOOLDOWN = 5.0f;
    protected AI ai;
    protected float maxVelocity, visualMultiplier, visibility;

    protected boolean active;
    protected Vector2 position;
    protected float angleFacing, velocity;

//    protected boolean inShade;

    public Agent(World world, Settings settings)
    {
        this.world = world;
        this.settings = settings;

        maxVelocity = MAXVELOCITY;
        visualMultiplier = 1.0f;

        active = false;
    }

    public void spawn(Vector2 position, float angleFacing)
    {
        active = true;
        this.position = position;
        this.angleFacing = angleFacing;
        velocity = 0f;
    }

    public void despawn()
    {
        active = false;
        velocity = 0.0f;
    }

    public void spawnRandom(Map map)
    {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        spawn(randomPosition, (float)Math.random() * 360.0f);
    }

    public void update()
    {
        float newVelocity = ai.getNewVelocity();

        newVelocity = newVelocity < 0.0f ? 0.0f : (newVelocity > maxVelocity ? maxVelocity : newVelocity);

        velocity = newVelocity;
        System.out.println(velocity);


        float newAngleFacing = ai.getNewAngle();

        newAngleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;

        float angleDifference = newAngleFacing - angleFacing;
        angleDifference += angleDifference > 180.0f ? -360.0f : angleDifference <= -180.0f ? 360.0f : 0;

        if(Math.abs(angleDifference) >= 45.0f / GameLoop.TICKRATE)
        {
            //TODO: descrease visibility, start blindness timer

            if(Math.abs(angleDifference) > 180.0f / GameLoop.TICKRATE)
                newAngleFacing = angleFacing + (float) (Math.signum(angleDifference) * 180.0f / GameLoop.TICKRATE);
        }

        angleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;
    }

    private Vector2 newPosition;
    private float velocityX, velocityY, angleRad;

    public void updatePosition()
    {
        angleRad = (float)Math.toRadians(angleFacing);

        velocityX = velocity * (float)Math.cos(angleRad);
        velocityY = velocity * (float)Math.sin(angleRad);

        newPosition = new Vector2((float) (position.x + velocityX / GameLoop.TICKRATE), (float) (position.y + velocityY / GameLoop.TICKRATE));

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

        if(active)
            for(Agent agent : world.getAgents())
                if (agent != this && position.dst2(agent.position) < (agent.visibility * agent.visibility))
                    visibleAgents.add(agent);

        return visibleAgents;
    }

    public ArrayList<Guard> getVisibleGuards()
    {
        ArrayList<Guard> visibleGuards = new ArrayList<>();

        if(active)
            for(Guard guard : world.getGuards())
                if (guard != this && position.dst2(guard.position) < (guard.visibility * guard.visibility))
                    visibleGuards.add(guard);

        return visibleGuards;
    }

    public ArrayList<Intruder> getVisibleIntruders()
    {
        ArrayList<Intruder> visibleIntruders = new ArrayList<>();

        if(active)
            for(Intruder intruder : world.getIntruders())
                if (intruder != this && position.dst2(intruder.position) < (intruder.visibility * intruder.visibility))
                    visibleIntruders.add(intruder);

        return visibleIntruders;
    }

    public ArrayList<Float> getVisibleSounds()
    {
        ArrayList<Sound> visibleSounds = new ArrayList<>();

        if(active)
            for(Sound sound : world.getSounds())
                if (position.dst2(sound.getPosition()) < (sound.getVisibility() * sound.getVisibility()))
                    visibleSounds.add(sound);

        ArrayList<Float> soundAngles = new ArrayList<>();

        Random random = new Random();

        for(Sound sound : visibleSounds)
        {
            float soundAngle = (float)(Math.atan2(sound.getPosition().y - position.y, sound.getPosition().x - position.x) / Math.PI * 180.0f);

            soundAngle += random.nextGaussian() * 10.0f;

            soundAngle = (soundAngle % 360.0f + 360.0f) % 360.0f;

            soundAngles.add(soundAngle);
        }

        return soundAngles;
    }

    public ArrayList<Pheromone> getVisiblePheromones()
    {
        ArrayList<Pheromone> visiblePheromones = new ArrayList<>();

        if(active)
            for(Pheromone pheromone : world.getPheromones())
                if (position.dst2(pheromone.getPosition()) < (pheromone.getVisibility() * pheromone.getVisibility()))
                    visiblePheromones.add(pheromone);

        return visiblePheromones;
    }

    private int lastPheromoneTick = (int)(-PHEROMONECOOLDOWN * GameLoop.TICKRATE);

    public boolean createPheromone(Pheromone.PheromoneType pheromoneType)
    {
        int currentTick = world.getGameLoop().getTicks();

        if(currentTick - lastPheromoneTick >= (int)(PHEROMONECOOLDOWN * GameLoop.TICKRATE))
        {
            world.addPheromone(new Pheromone(pheromoneType, new Vector2(position)));
            lastPheromoneTick = currentTick;

            return true;
        }

        return false;
    }

    public boolean getActive()
    {
        return active;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public float getAngleFacing()
    {
        return angleFacing;
    }

    public void setAngleFacing(float angleFacing) {
        this.angleFacing = angleFacing;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
