package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.*;

import java.util.ArrayList;
import java.util.Random;

abstract public class Agent
{
    protected World world;

    public static final float VISUAL_ANGLE = 45.0f, MAX_VELOCITY = 1.4f, MAX_TURN_VELOCITY = 180.0f, PHEROMONE_COOL_DOWN = 10.0f, FAST_TURN_COOL_DOWN = 0.5f, SENTRYTOWER_COOL_DOWN = 2.0f;
    protected AI ai;
    protected float maxVelocity, visualMultiplier, visibility;
    private float maxTimeSamePosition = 3.0f;

    protected boolean active, fastTurning;
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

    public void spawn(Vector2 position, float angleFacing)
    {
        active = true;
        this.position = position;
        this.angleFacing = angleFacing;
        velocity = 0f;
    }

    public void aiSpawn()
    {
        ai.spawn(world.getMap());
    }

    public void despawn()
    {
        active = false;
        velocity = 0.0f;
    }

    private int lastFastTurnTick = (int)(-FAST_TURN_COOL_DOWN * GameLoop.TICK_RATE), firstSentryTowerTick = (int)(-SENTRYTOWER_COOL_DOWN * GameLoop.TICK_RATE);
    private boolean transversingSentryTower;

    public void update()
    {
        if(active)
        {
            transversingSentryTower = world.getGameLoop().getTicks() - firstSentryTowerTick < (int) (SENTRYTOWER_COOL_DOWN * GameLoop.TICK_RATE);

            //Set visual multiplier
            if(((world.getGameLoop().getTicks() - lastFastTurnTick) < (int) (FAST_TURN_COOL_DOWN * GameLoop.TICK_RATE)) || transversingSentryTower)
            {
                visualMultiplier = 0.0f;
            }
            else if(inSentryTower())
                visualMultiplier = SentryTower.VISUAL_MULTIPLIER;
            else if(inShade())
                visualMultiplier = Shade.VISUAL_MULTIPLIER;
            else
                visualMultiplier = 1.0f;

            //Update AI
            ai.update();

            if(transversingSentryTower)
            {
                velocity = 0.0f;
            }
            else
            {
                //Restrict, then update velocity
                float newVelocity = ai.getNewVelocity();

                newVelocity = newVelocity < 0.0f ? 0.0f : (newVelocity > maxVelocity ? maxVelocity : newVelocity);

                velocity = newVelocity;

                //Restrict, then update angleFacing
                float newAngleFacing = ai.getNewAngle();

                newAngleFacing = modulo(newAngleFacing, 360.0f);

                float angleDifference = newAngleFacing - angleFacing;
                angleDifference += angleDifference > 180.0f ? -360.0f : angleDifference <= -180.0f ? 360.0f : 0;

                if (Math.abs(angleDifference) >= 45.0f / GameLoop.TICK_RATE)
                {
                    lastFastTurnTick = world.getGameLoop().getTicks();

                    if (Math.abs(angleDifference) > 180.0f / GameLoop.TICK_RATE)
                        newAngleFacing = angleFacing + (float)(Math.signum(angleDifference) * 180.0f / GameLoop.TICK_RATE);
                }

                angleFacing = modulo(newAngleFacing, 360.0f);

                angleFacing = newAngleFacing;
            }
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
        }
    }

    public boolean isValidMove(Vector2 position, Vector2 newPosition)
    {
        if(Area.intersects(position, newPosition, new Vector2(0,world.getMap().getHeight()), new Vector2(world.getMap().getWidth(),world.getMap().getHeight()), new Vector2(0,0), new Vector2(world.getMap().getWidth(), 0)))
            return false;

        for(SentryTower sentryTower: world.getMap().getSentryTowers())
        {
            if(sentryTower.intersects(position, newPosition))
            {
                if(this instanceof Guard)
                {
                    firstSentryTowerTick = world.getGameLoop().getTicks();
                    return true;
                }
                else
                    return false;
            }
        }

        for(Structure structure: world.getMap().getStructures())
        {
            if(structure.intersects(position, newPosition))
            {
                for (Entrance entrance : structure.getEntrances()) {
                    if (Area.intersects(position, newPosition, entrance.getStartPosition(), entrance.getEndPosition())) {
                        world.addSound(new Sound(position, entrance.getSoundDistance()));
                        return true;
                    }
                }
                return false;
            }
        }

        return true;
    }

    public boolean agentVisible(Agent agent)
    {
        if(active)
            if (agent.active && agent != this)
            {
                for(Structure structure: world.getMap().getStructures())
                {
                    if(structure.contains(position))
                    {
                        return structure.contains(agent.position);
                    }
                }

                if(position.dst2(agent.position) < (agent.visibility * visualMultiplier * agent.visibility * visualMultiplier))
                    return pointInVisualAngle(agent.position);
            }

        return false;
    }

    public ArrayList<Guard> getVisibleGuards()
    {
        ArrayList<Guard> visibleGuards = new ArrayList<>();

        if(active)
            for(Guard guard : world.getGuards())
                if(agentVisible(guard))
                    visibleGuards.add(guard);

        return visibleGuards;
    }

    public Vector2[] getVisibleBounds() {

        Vector2[] visibleBounds = new Vector2[] {new Vector2(-5,-5), //top
            new Vector2(-5,-5), //bot
            new Vector2(-5,-5), //right
            new Vector2(-5,-5), //left
        };

        float worldHeight = world.getMap().getHeight();
        float worldWidth = world.getMap().getWidth();

        //in this case visibility == VISUAL_RANGE
        if (active) {

            float[] checkAngles = new float[]{(float)Math.tan(Math.toRadians(angleFacing - VISUAL_ANGLE * 0.5f)), //beginAngle
                (float)Math.tan(Math.toRadians(angleFacing)), //midAngle
                (float)Math.tan(Math.toRadians(angleFacing + VISUAL_ANGLE * 0.5f)) //endAngle
            };

            for (float angle : checkAngles) {
                float bias = position.y - angle * position.x;
//                System.out.println(position.x + " " + position.y);
                if (position.dst2(new Vector2((worldHeight-bias)/angle,worldHeight+1)) < (visibility * visibility)) {
                    //check if it sees the top border
//                    System.out.println("Detected top wall");
                    visibleBounds[0] = new Vector2((worldHeight-bias)/angle,worldHeight+1);
                }
                else if (position.dst2(new Vector2(-bias,1)) < (visibility * visibility)) {
                    //check if it sees the bot border
//                    System.out.println("Detected bot wall");
                    visibleBounds[1] = new Vector2(-bias,1);
                }
                else if (position.dst2(new Vector2(worldWidth+1,angle*worldWidth+bias)) < (visibility * visibility)) {
                    //check if it sees the right border
//                    System.out.println("Detected right wall");
                    visibleBounds[2] = new Vector2(worldWidth+1,angle*worldWidth+bias);
                }
                else if (position.dst2(new Vector2(-1,bias)) < (visibility * visibility)) {
                    //check if it sees the left border
//                    System.out.println("Detected left wall");
                    visibleBounds[3] = new Vector2(-1,bias);
                }
            }
        }


        return visibleBounds;
    }

    public ArrayList<Intruder> getVisibleIntruders()
    {
        ArrayList<Intruder> visibleIntruders = new ArrayList<>();

        if(active)
            for(Intruder intruder : world.getIntruders())
                if(agentVisible(intruder))
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

            if (currentTick - lastPheromoneTick >= (int) (PHEROMONE_COOL_DOWN * GameLoop.TICK_RATE))
            {
                world.addPheromone(new Pheromone(pheromoneType, new Vector2(position)));
                lastPheromoneTick = currentTick;

                return true;
            }
        }
        return false;
    }

    public boolean inSentryTower()
    {
        for(SentryTower sentryTower: world.getMap().getSentryTowers())
            if(sentryTower.contains(position))
                return true;

        return false;
    }

    public boolean inShade()
    {
        for(Shade shade: world.getMap().getShades())
            if(shade.contains(position))
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

    public boolean pointInVisualAngle(Vector2 point)
    {
        float beginAngle = modulo(angleFacing - VISUAL_ANGLE * 0.5f, 360.0f);
        float endAngle = modulo(angleFacing + VISUAL_ANGLE * 0.5f, 360.0f);
        float angleBetweenAgents = modulo((float)(getAngleBetweenTwoPos(position, point)),360.0f);

        if(endAngle < beginAngle)
            return (angleBetweenAgents >= 0 && angleBetweenAgents <= endAngle) || (angleBetweenAgents >= beginAngle && angleBetweenAgents <= 360);
        else
            return angleBetweenAgents >= beginAngle && angleBetweenAgents <= endAngle;
    }

    public AI getAi() {
        return ai;
    }

    public float getMaxVelocity()
    {
        return maxVelocity;
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

    public void setVelocity(float velocity){ this.velocity = velocity;};

    public float getVisualMultiplier()
    {
        return visualMultiplier;
    }

    public World getWorld() {
        return world;
    }

    public float getVisibility(){return visibility;}

    public int getNumberOfGuards(){return world.getGuards().size();}

}
