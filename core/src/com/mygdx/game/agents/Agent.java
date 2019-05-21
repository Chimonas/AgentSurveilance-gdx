package com.mygdx.game.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.ai.AI;
import com.mygdx.game.areas.Area;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

import java.util.ArrayList;

abstract public class Agent
{
    protected Settings settings;

    protected AI ai;
    protected float maxVelocity;

    protected boolean active;

    protected Vector2 position;
    protected float angleFacing;
    protected float newAngleFacing;
    protected float turnAngle;

    protected float turnVelocity;
    protected float velocity;
    protected float noiseGeneratedRadius; //The sound one makes depending on his speed
    protected float audioCapability;
    protected float visibility;

    //Visual Requirements --> depends how visibility/vision is implemented
    protected float[] visualRange;
    protected float visualAngle;

//    protected boolean inShade;

    public Agent(Settings settings, float visibility)
    {
        this.settings = settings;
        this.maxVelocity = 1.4f;
        this.visualAngle = 45.0f;
        this.visibility = visibility; //at which distance others can see him
        this.turnVelocity = 180.0f;
        this.audioCapability = 1; //0 if one can't hear anything
        setNoiseGeneratedRadius();
        this.active = false;

    }

    public void spawn(Vector2 position, float angle)
    {
        active = true;
        this.position = position;
        this.angleFacing = angle;
        velocity = 0f;
    }

    public void spawnRandomPosition(Map map)
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
        ai.update(velocity, angleFacing);

        velocity = ai.getVelocity();
        angleFacing = ai.getAngle();

        updatePosition();
    }

    private float velocityX, velocityY, angleRad;

    public void updatePosition()
    {
        angleRad = (float)Math.cos(Math.toRadians(angleFacing));

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

    public float getAngleFacing() {
        return angleFacing;
    }

    public void setAngleFacing(float angleFacing) {
        this.angleFacing = angleFacing;
    }

    public float getNewAngleFacing(){return this.newAngleFacing; }

    public void setNewAngleFacing(float newAngleFacing){this.newAngleFacing = newAngleFacing; }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getTurnVelocity() { return turnVelocity; }

    public void setTurnVelocity(float turnVelocity) { this.turnVelocity = turnVelocity; }

    public float[] getVisualRange() {
        return visualRange;
    }

    public void setVisualRange(float visualRangeBeg, float visualRangeEnd) {

        float[] visualRange = {visualRangeBeg, visualRangeEnd};
        this.visualRange = visualRange;
    }

    public float getVisualAngle() {
        return visualAngle;
    }

    public void setVisualAngle(float visualAngle) {
        this.visualAngle = visualAngle;
    }

    public float getTurnAngle(){return this.turnAngle; }

    public void setTurnAngle(float turnAngle){
        this.turnAngle = turnAngle;
    }

    public float getAudioCapability() { return audioCapability; }

    public void setAudioCapability(float audioCapability){ this.audioCapability = audioCapability; }

    public void setVisibility(float visibility) { this.visibility = visibility; }

    public float getVisibility() { return visibility; }

    public float getNoiseGeneratedRadius(){ return noiseGeneratedRadius;};

    //To be called at every frame in order to update sound according to speed
    public void setNoiseGeneratedRadius() {

        if(velocity < 0.5){
            this.noiseGeneratedRadius = 1.0f;
            return;
        }
        else if(velocity < 1) {
            this.noiseGeneratedRadius = 3.0f;
            return;
        }
        else if(velocity < 2) {
            this.noiseGeneratedRadius = 5.0f;
            return;
        }
        else this.noiseGeneratedRadius = 10.0f;
    }

    public static void updateAngleFacing(ArrayList<Area> areas, Agent a, float newAngle){
        ArrayList<Agent> agentsInVision = new ArrayList<Agent>();

        float oldAngle = a.getAngleFacing();
        a.setTurnAngle(oldAngle - newAngle);
        a.setVelocity(0);
        a.setAngleFacing(oldAngle - (a.getTurnAngle()/a.getTurnVelocity()));
        a.setTurnAngle(newAngle - a.getAngleFacing());

    }


//    public void render(ShapeRenderer shapeRenderer, boolean showNoiseRadius, boolean showVisualRange){
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(position.x, position.y,(float)1);
//        shapeRenderer.end();
//
//        if(showNoiseRadius){
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle(position.x, position.y,(float)1 + noiseGeneratedRadius);
//            shapeRenderer.end();
//        }
//
//        if(showVisualRange){
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(new Color(1, 0, 0, 0.5f));
//            shapeRenderer.arc(position.x, position.y, visualRange[1], angleFacing, visualAngle);
//            shapeRenderer.end();
//            Gdx.gl.glDisable(GL20.GL_BLEND);
//        }
//    }

}
