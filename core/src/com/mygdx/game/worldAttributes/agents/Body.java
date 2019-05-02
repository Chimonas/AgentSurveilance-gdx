//package com.mygdx.game.worldAttributes.agents;
//
//import com.badlogic.gdx.graphics.Texture;
//
////Here will be computed the movement, turning, visual, hearing
//public class Body {
//
//    //Would be nice to reorganize this
//    //Feels like some classes such as visual, movement, hearing are not necessary
//
//    private Texture texture;
//
//    //make part of agents
//    protected float audioCapability;
//    protected float visibility = 10.0f; //at which distance can other agents see him
//
//    //Visual requirements
//    protected float[] visualRange;
//    protected float visualAngle;
//    protected Visual visualArea;
//            //= new Visual(position, angleFacing, visualAngle, visualRange);
//
//    private float restTime;
//    private float runTime;
//    private boolean canRun;
//
//    public void stand() {
//        if (getMovementState() == 2) {
//            restTime = System.currentTimeMillis();
//        }
//        velocity = 0;
//    }
//
//    public void walk() {
//        if (getMovementState() == 2) {
//            restTime = System.currentTimeMillis();
//        }
//        velocity = 1.4f;
//    }
//
//    public void run() {
//        if (canRun) {
//           velocity = 3.0f;
//           runTime = System.currentTimeMillis();
//           canRun = false;
//        }
//    }
//
//    public void randomMovement(){
//
//        //Change of movement state
//        double nextState = Math.random();
//        if(nextState < 0.9) setVelocity(1.4f);
//        else if(nextState < 0.95 && nextState > 0.9) setVelocity(0.0f);
//        else setVelocity(3.0f);
//
//        //Change of direction
//        if(Math.random()>0.99){
//            setAngleFacing((float) (Math.random()*360.0));
//        }
//    }
//
//    //called in every frame
//    public void checkRun() {
//        //Must rest for 10 seconds
//        if (System.currentTimeMillis() - restTime > 10000) {
//            canRun = true;
//        }
//
//        //can only run for 5 seconds
//        if (getMovementState() == 2) {
//            if (System.currentTimeMillis() - runTime > 5000) {
//                walk();
//            }
//        }
//    }
//
//    public int getMovementState() {
//        // 0 for standing
//        // 1 for walking
//        // 2 for running/sprint
//        if (velocity == 0) {
//            return 0;
//        } else if (velocity > 2) {
//            return 2;
//        } else
//            return 1;
//    }
//
//
//
//    //Soooo many getters and setters
//
//    public Texture getTexture(){ return texture;};
//
//    public void setTexture(Texture t){ this.texture = t;};
//
//    public float getAngleFacing() {
//        return angleFacing;
//    }
//
//    public void setAngleFacing(float angleFacing) {
//        this.angleFacing = angleFacing;
//    }
//
//    public float getNewVelocity() {
//        return velocity;
//    }
//
//    public void setVelocity(float velocity) {
//        this.velocity = velocity;
//    }
//
//    public float[] getVisualRange() {
//        return visualRange;
//    }
//
//    public void setVisualRange(float visualRangeBeg, float visualRangeEnd) {
//
//        float[] visualRange = {visualRangeBeg, visualRangeEnd};
//        this.visualRange = visualRange;
//    }
//
//    public double[] getPosition() {
//        return position;
//    }
//
//    public void setPosition(double[] position) {
//        this.position = position;
//    }
//
//    public void setXPosition(double x){
//        this.position[0] = x;
//    }
//
//    public void setYPosition(double y){
//        this.position[1] = y;
//    }
//
//    public float getRestTime() {
//        return restTime;
//    }
//
//    public void setRestTime(float restTime) {
//        this.restTime = restTime;
//    }
//
//    public float getRunTime() {
//        return runTime;
//    }
//
//    public void setRunTime(float runTime) {
//        this.runTime = runTime;
//    }
//
//    public boolean getCanRun() {
//        return canRun;
//    }
//
//    public void setCanRun(boolean canRun) {
//        this.canRun = canRun;
//    }
//
//    public float getVisualAngle() {
//        return visualAngle;
//    }
//
//    public void setVisualAngle(float visualAngle) {
//        this.visualAngle = visualAngle;
//    }
//
//    public float getAudioCapability() { return audioCapability; }
//
//    public void setAudioCapability(float audioCapability){ this.audioCapability = audioCapability; }
//
//    public void setVisibility(float visibility) { this.visibility = visibility; }
//
//    public float getVisibility() { return visibility; }
//
//    public Visual getVisualArea() {
//        return visualArea;
//    }
//
//    public void setVisualArea(Visual visualArea) {
//        this.visualArea = visualArea;
//    }
//
//}
