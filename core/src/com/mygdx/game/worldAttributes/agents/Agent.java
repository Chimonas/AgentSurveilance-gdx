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

    public final float VISUAL_ANGLE = 45.0f, MAX_VELOCITY = 1.4f, MAX_TURN_VELOCITY = 180.0f, PHEROMONE_COOL_DOWN = 20.0f;
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

            newAngleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;

            float angleDifference = newAngleFacing - angleFacing;
            angleDifference += angleDifference > 180.0f ? -360.0f : angleDifference <= -180.0f ? 360.0f : 0;

            if (Math.abs(angleDifference) >= 45.0f / GameLoop.TICK_RATE)
            {
                //TODO: descrease visibility, start blindness timer

                if (Math.abs(angleDifference) > 180.0f / GameLoop.TICK_RATE)
                    newAngleFacing = angleFacing + (float)(Math.signum(angleDifference) * 180.0f / GameLoop.TICK_RATE);
            }
            angleFacing = (newAngleFacing % 360.0f + 360.0f) % 360.0f;

//            angleFacing = newAngleFacing;

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

//            System.out.println("New angle facing: " + angleFacing);
//            System.out.println("Old Pos : " + position.x + " " + position.y);
//            System.out.println("New Pos : " + newPosition.x + " " + newPosition.y);
//            System.out.println("Velocity: " + velocityX + " " + velocityY);
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
                if(getAgentVisible(intruder))
                    visibleIntruders.add(intruder);

        return visibleIntruders;
    }

    private ArrayList<Vector2[]> visibleSentryTowers = new ArrayList<>();
    private ArrayList<Vector2[]> visibleShade = new ArrayList<>();
    private ArrayList<Vector2[]> visibleStructure = new ArrayList<>();


    public ArrayList<Vector2[]> getVisibleSentryTowers() {
        return visibleSentryTowers;
    }

    public ArrayList<Vector2[]> getVisibleShade() {
        return visibleShade;
    }

    public ArrayList<Vector2[]> getVisibleStructure() {
        return visibleStructure;
    }


    public void updateVisibleAreas() {
        ArrayList<Area> visibleAreas = new ArrayList<>();

        if (active)
            for (Area a : world.getMap().getAreaList()) {
                if (getAreaVisible(a)) {

                    float[] checkAngles = new float[]{(float) Math.tan(Math.toRadians(angleFacing - VISUAL_ANGLE * 0.5f)),
                        (float) Math.tan(Math.toRadians(angleFacing + VISUAL_ANGLE * 0.5f))};
                    //get visible bounds
                    //set them to the arraylist corresponding their own kind of area
                    Vector2[] visibleBounds = getVisibleBounds(checkAngles,a.getTopLeft(),a.getBottomRight());
                    visibleSentryTowers = new ArrayList<>();
                    visibleStructure = new ArrayList<>();
                    visibleShade = new ArrayList<>();
                    if (a instanceof SentryTower) visibleSentryTowers.add(visibleBounds);
                    if (a instanceof Shade) visibleShade.add(visibleBounds);
                    if (a instanceof Structure) visibleStructure.add(visibleBounds);


                }
            }

    }

    public boolean getAreaVisible(Area area) {

        if(active) {

            if ((position.dst2(new Vector2(area.getBottomRight().x,position.y)) < area.getVisibility() * area.getVisibility()) ||
                position.dst2(new Vector2(area.getTopLeft().x,position.y)) < area.getVisibility() * area.getVisibility() ||
                position.dst2(new Vector2(position.x,area.getBottomRight().y)) < area.getVisibility() * area.getVisibility() ||
                position.dst2(new Vector2(position.x,area.getTopLeft().y)) < area.getVisibility() * area.getVisibility() ) {

                return true;
            }
        }
        return false;
    }


    public Vector2[] getVisibleBounds(float[] anglesToCheck,Vector2 topLeft, Vector2 botRight) {
        Vector2[] visibleBounds = {new Vector2(-5,-5),
                                    new Vector2(-5,-5),
                                    new Vector2(-5,-5),
                                    new Vector2(-5,-5),};

        for (float angle : anglesToCheck) {
            float bias = position.y - angle * position.x;
//                System.out.println(position.x + " " + position.y);
            if (position.dst2(new Vector2((topLeft.y-bias)/angle,topLeft.y)) < (visibility * visibility)) {
                //check if it sees the top border
//                    System.out.println("Detected top wall");
                visibleBounds[0] = new Vector2((topLeft.y-bias)/angle,topLeft.y+1);
            }
            else if (position.dst2(new Vector2((botRight.y-bias)/angle,botRight.y)) < (visibility * visibility)) {
                //check if it sees the bot border
                    System.out.println("Detected bot wall");
                visibleBounds[1] = new Vector2((botRight.y-bias)/angle,botRight.y);
            }
            else if (position.dst2(new Vector2(botRight.x,angle*botRight.x+bias)) < (visibility * visibility)) {
                //check if it sees the right border
//                    System.out.println("Detected right wall");
                visibleBounds[2] = new Vector2(botRight.x,angle*botRight.x+bias);
            }
            else if (position.dst2(new Vector2(topLeft.x,angle*topLeft.x+bias)) < (visibility * visibility)) {
                //check if it sees the left border
//                    System.out.println("Detected left wall");
                visibleBounds[3] = new Vector2(topLeft.x,angle*topLeft.x+bias);
            }
        }
        return visibleBounds;
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

    public void setVelocity(float velocity){ this.velocity = velocity;};

    public World getWorld() {
        return world;
    }

}
