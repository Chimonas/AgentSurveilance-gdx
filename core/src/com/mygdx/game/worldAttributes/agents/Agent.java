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

    public final float VISUAL_ANGLE = 45.0f, MAX_VELOCITY = 1.4f, MAX_TURN_VELOCITY = 180.0f, PHEROMONE_COOL_DOWN = 10.0f;
    protected AI ai;
    protected float maxVelocity, visualMultiplier, visibility;
    private float maxTimeSamePosition = 3.0f;

    protected boolean active;
    protected Vector2 position;
    protected float angleFacing, velocity;

//    protected boolean inShade;

    public Agent(World world)
    {
        this.world = world;

        maxVelocity = MAX_VELOCITY;
        visualMultiplier = 1.0f;

        active = false;
    }

    public void spawnPosition(Vector2 position, float angleFacing)
    {
        active = true;
        this.position = position;
        this.angleFacing = angleFacing;
        velocity = 0f;
    }

    public void spawn()
    {

        ai.spawn(world.getMap());
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

            //MAybe fucks up
            //IT GIVES A BIGGER ANGLE THAN 360

//            newAngleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;
//
//            float angleDifference = newAngleFacing - angleFacing;
//            angleDifference += angleDifference > 180.0f ? -360.0f : angleDifference <= -180.0f ? 360.0f : 0;
//
//            if (Math.abs(angleDifference) >= 45.0f / GameLoop.TICK_RATE)
//            {
//                //TODO: descrease visibility, start blindness timer
//
//                if (Math.abs(angleDifference) > 180.0f / GameLoop.TICK_RATE)
//                    newAngleFacing = angleFacing + (float)(Math.signum(angleDifference) * 180.0f / GameLoop.TICK_RATE);
//            }
//            angleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;

            angleFacing = newAngleFacing;

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

            newPosition = new Vector2((float) (position.x + velocityX / GameLoop.TICK_RATE), (float) (position.y + velocityY / GameLoop.TICK_RATE));


            if (isValidMove(position, newPosition))
                position.set(newPosition);
            else
                velocity = 0.0f;

            System.out.println("New angle facing: " + angleFacing);
            System.out.println("Old Pos : " + position.x + " " + position.y);
            System.out.println("New Pos : " + newPosition.x + " " + newPosition.y);

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
                float beginAngle = modulo(angleFacing - VISUAL_ANGLE * 0.5f, 360.0f);
                float endAngle = modulo(angleFacing + VISUAL_ANGLE * 0.5f, 360.0f);
                float angleBetweenAgents = modulo((float)(getAngleBetweenTwoPos(position, agent.getPosition())),360.0f);

                if(endAngle< beginAngle)
                    return (angleBetweenAgents >= 0 && angleBetweenAgents <= endAngle) ||
                            (angleBetweenAgents >= beginAngle && angleBetweenAgents <= 360);
                else return angleBetweenAgents >= beginAngle && angleBetweenAgents <= endAngle;
            }

        return false;
    }

    public ArrayList<Guard> getVisibleGuards()
    {
        ArrayList<Guard> visibleGuards = new ArrayList<>();

        if(active)
            for(Guard guard : world.getGuards())
                if(getAgentVisible(guard))
                    visibleGuards.add(guard);

        return visibleGuards;
    }

    public ArrayList<Intruder> getVisibleIntruders()
    {
        ArrayList<Intruder> visibleIntruders = new ArrayList<>();

        if(active)
            for(Intruder intruder : world.getIntruders())
                if(getAgentVisible(intruder))
                    visibleIntruders.add(intruder);


        return visibleIntruders;
    }


    public ArrayList<Communication> getReceivedCommunications()
    {
        ArrayList<Communication> receivedCommunications = new ArrayList<>();

        if(active)
            for (Communication communication : world.getCommunications())
                if (communication.getReceivingAgent() == this)
                    receivedCommunications.add(communication);

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
            float soundAngle = getAngleBetweenTwoPos(position, sound.getPosition());

            soundAngle += random.nextGaussian() * 10.0f; //Adding random factor with standard deviation of 10 degrees

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

    private int lastPheromoneTick = (int)(-PHEROMONE_COOL_DOWN * GameLoop.TICK_RATE);

    public boolean createPheromone(Pheromone.PheromoneType pheromoneType)
    {
        if(active)
        {
            int currentTick = world.getGameLoop().getTicks();

            if (currentTick - lastPheromoneTick >= (int) (PHEROMONE_COOL_DOWN * GameLoop.TICK_RATE)) {
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

    public boolean inShade()
    {
        for(Area area: world.getMap().getAreaList())
            if(area instanceof Shade && area.contains(position))
                return true;

        return false;
    }

    public float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }

    public float getAngleBetweenTwoPos(Vector2 pos1, Vector2 pos2)
    {
        return (float)(Math.toDegrees(Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x)));
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
