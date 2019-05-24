package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.Communication;
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

    public final float VISUALANGLE = 45.0f, MAXVELOCITY = 1.4f, MAXTURNVELOCITY = 180.0f, PHEROMONECOOLDOWN = 10.0f;
    protected AI ai;
    protected float maxVelocity, visualMultiplier, visibility;

    protected boolean active;
    protected Vector2 position;
    protected float angleFacing, velocity;

//    protected boolean inShade;

    public Agent(World world)
    {
        this.world = world;

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

    public void update()
    {
        if(active)
        {
            ai.update();

            float newVelocity = ai.getNewVelocity();

            newVelocity = newVelocity < 0.0f ? 0.0f : (newVelocity > maxVelocity ? maxVelocity : newVelocity);

            velocity = newVelocity;

            float newAngleFacing = ai.getNewAngle();

            newAngleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;

            float angleDifference = newAngleFacing - angleFacing;
            angleDifference += angleDifference > 180.0f ? -360.0f : angleDifference <= -180.0f ? 360.0f : 0;

            if (Math.abs(angleDifference) >= 45.0f / GameLoop.TICKRATE) {
                //TODO: descrease visibility, start blindness timer

                if (Math.abs(angleDifference) > 180.0f / GameLoop.TICKRATE)
                    newAngleFacing = angleFacing + (float)(Math.signum(angleDifference) * 180.0f / GameLoop.TICKRATE);
            }

            angleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;


            world.addSound(new Sound(new Vector2(position), velocity * 4.0f));
        }
    }

    private Vector2 newPosition;
    private float velocityX, velocityY, angleRad;

    public void updatePosition() //TODO: skidding along walls
    {
        if(active)
        {
            angleRad = (float) Math.toRadians(angleFacing);

            velocityX = velocity * (float) Math.cos(angleRad);
            velocityY = velocity * (float) Math.sin(angleRad);

            newPosition = new Vector2((float) (position.x + velocityX / GameLoop.TICKRATE), (float) (position.y + velocityY / GameLoop.TICKRATE));

            if (isValidMove(position, newPosition))
                position.set(newPosition);
            else
                velocity = 0.0f;
        }
    }

    public boolean isValidMove(Vector2 position, Vector2 newPosition)
    {
        if(Area.intersects(position, newPosition, new Vector2(0,world.getMap().getHeight()), new Vector2(world.getMap().getWidth(),world.getMap().getHeight()), new Vector2(0,0), new Vector2(world.getMap().getWidth(), 0)))
            return false;

        for (Area area : world.getMap().getAreaList())
            if (!(area instanceof Shade || area instanceof Target) && area.intersects(position, newPosition)) return false;

        return true;
    }

    public boolean getAgentVisible(Agent agent)
    {
        if(active)
            if (agent.active && agent != this && position.dst2(agent.position) < (agent.visibility * agent.visibility))
            {
                float beginAngle = modulo(angleFacing - VISUALANGLE * 0.5f, 360.0f);
                float endAngle = modulo(angleFacing + VISUALANGLE * 0.5f, 360.0f);
                float agentAngle = modulo((float)(Math.toDegrees(Math.atan2(agent.getPosition().y - position.y, agent.getPosition().x - position.x))),360.0f);

                System.out.println("I see him!!");
                return agentAngle >= beginAngle && agentAngle <= endAngle;
            }

        return false;
    }

    public ArrayList<Guard> getVisibleGuards()
    {
        ArrayList<Guard> visibleGuards = new ArrayList<>();

        if(active)
        {
            for(Guard guard : world.getGuards())
                if(getAgentVisible(guard))
                    visibleGuards.add(guard);
        }

        return visibleGuards;
    }

    public ArrayList<Intruder> getVisibleIntruders()
    {
        ArrayList<Intruder> visibleIntruders = new ArrayList<>();

        if(active)
        {
            for(Intruder intruder : world.getIntruders())
                if(getAgentVisible(intruder))
                    visibleIntruders.add(intruder);
        }

        return visibleIntruders;
    }

    public ArrayList<Communication> getReceivedCommunications()
    {
        ArrayList<Communication> receivedCommunications = new ArrayList<>();

        if(active)
            for (Communication communication : world.getCommunications())
            {
                if (communication.getReceivingAgent() == this)
                {
                    receivedCommunications.add(communication);
                }
            }

        return receivedCommunications;
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
            float soundAngle = (float)(Math.toDegrees(Math.atan2(sound.getPosition().y - position.y, sound.getPosition().x - position.x)));

            soundAngle += random.nextGaussian() * 10.0f;

            soundAngle = modulo(soundAngle, 360.0f);

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
        if(active)
        {
            int currentTick = world.getGameLoop().getTicks();

            if (currentTick - lastPheromoneTick >= (int) (PHEROMONECOOLDOWN * GameLoop.TICKRATE)) {
                world.addPheromone(new Pheromone(pheromoneType, new Vector2(position)));
                lastPheromoneTick = currentTick;

                return true;
            }
        }
        return false;
    }

    public boolean createCommunication(Agent receivingAgent, ArrayList<?> message)
    {
        if(active)
            if(receivingAgent != this)
            {
                world.addCommunication(new Communication(this, receivingAgent, message));
                return true;
            }
        return false;
    }

    public float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
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

    public float getVelocity() {
        return velocity;
    }

    public World getWorld() {
        return world;
    }
}
