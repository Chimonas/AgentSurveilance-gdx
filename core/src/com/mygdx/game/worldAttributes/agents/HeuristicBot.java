package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.Pheromone;

import java.util.Arrays;
import java.util.LinkedList;

public class HeuristicBot extends AI {

    //Have an array of all angles

    private float[][] angles;
    private float[] coefficients;
    private final int DEGREE_LEVEL = 360; //needs be a divisor of 360
    private float[] best_actions = new float[DEGREE_LEVEL];
    public Vector2 oldPosition = new Vector2();
    PheromoneAI pherAI;
    private int samePositionTick = -1;
    boolean check = false;
    private float maxTimeSamePosition = 3.0f;

    public HeuristicBot(Agent agent, float[] coefficients) {
        super(agent);
        this.pherAI = new PheromoneAI(agent);
        this.newVelocity= agent.MAX_VELOCITY;
        this.coefficients = coefficients;

        for(int i = 0; i<best_actions.length; i++) best_actions[i] = (float)Math.random()*5;

        this.angles = new float[coefficients.length][DEGREE_LEVEL];

    }

    public void update() {

        super.update();
        this.pherAI.update();

        if(this.oldPosition.equals(agent.position) && !check){

            samePositionTick = agent.getWorld().getGameLoop().getTicks();
            check= true;
        }

        //HERE THE MEANING FOR EACH PHEROMONE IS SET
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.RED, PheromoneAI.PheromoneAction.FOLLOWINTRUDER);
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.BLUE, PheromoneAI.PheromoneAction.HEARDSOUND);
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.GREEN, PheromoneAI.PheromoneAction.FOUNDSHADE);

        this.best_actions = evaluateActions();

        updateNewAngle(agent.getAngleFacing());
        this.oldPosition.set(agent.position);
    }

    public float[] evaluateActions() {
        historyFade();
        getSoundVec();
        getAgents(); // guards, intruders
        getPherVec(); // red, green, blue, yellow
        getBorderVec();
        getAreaVec(); // sentry, shade, structures


        for (int i = 0; i < angles.length; i++)
            for (int j = 0; j < angles[i].length; j++)
                best_actions[j] += angles[i][j] * coefficients[i];
//        System.out.println(Arrays.toString(best_actions));
        return best_actions;
    }

    public void historyFade() {

        //new float[360][numberCoefficients];

        for (int i = 0; i < best_actions.length; i++) {
            float value = best_actions[i];
            best_actions[i] *= 0.95;
            if (Math.abs(best_actions[i] - value) < Math.pow(10,-15))
                best_actions[i] = 0;
        }

    }

    private void addAngles(float[][] addAngle) {

        for (int i = 0; i < addAngle.length; i++) {
            for (int j = 0; j < addAngle[i].length; j++) {
                angles[i][j] += addAngle[i][j];
            }
        }
    }

    @Override
    public void spawn(Map map) {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        agent.spawnPosition(randomPosition, (float)Math.random() * 360.0f);
    }

    public void updateNewAngle(float oldAngle) {
        float max = -100;
        float angle = oldAngle;
        for (int i = 0; i < best_actions.length; i++) {
            if (best_actions[i] >= max) {
                max = best_actions[i];
                angle = i;
            }
        }
        //System.out.println(angle + " " + oldAngle);
//        if(best_actions[(int)angle] <= best_actions[(int)oldAngle])
//            angle = oldAngle;

//        (agent.getWorld().getGameLoop().getTicks() - samePositionTick) > maxTimeSamePosition* GameLoop.TICK_RATE &&
        if(check) {

            if(agent.position.x < 1.0f) angle = (float) (Math.random()*180 + 270)%360;
            else if(agent.position.y < 1.0f) angle = (float) (Math.random()*180);
            else if(agent.position.x > agent.getWorld().getMap().getWidth()- 1.0f) angle = (float) (Math.random()*180 + 90) ;
            else if(agent.position.y > agent.getWorld().getMap().getHeight() - 1.0f) angle = (float) (Math.random()*180 + 180);
            else angle = (float) (Math.random() * 360.0);

//            angle = (oldAngle + 180)%360;
//            if (angle < 0)
//                angle = 360 + angle;
            samePositionTick = -1;
           check = false;
        }

        newAngle = angle*360/DEGREE_LEVEL;
    }

    public void getSoundVec() {
        float[] soundVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();
        for (float f : visibleSounds)
        {
            soundVec[(int) f*DEGREE_LEVEL/360] += 1;
        }
//TODO: put order of coefficients in the constructor
        this.angles[0] = soundVec;
    }

    public void getAgents() {
        float[] guardVec = new float[DEGREE_LEVEL];
        float[] intruderVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();
        for (Agent a : visibleGuards) {
            Vector2 other = a.getPosition();
            guardVec = distributedAngle(guardVec,10, (int) getPositiveAngleBetweenTwoPos(pos,other)*DEGREE_LEVEL/360,100);
        }
        for (Agent a : visibleIntruders) {
            Vector2 other = a.getPosition();
            intruderVec = distributedAngle(intruderVec, 10, (int) getPositiveAngleBetweenTwoPos(pos, other)*DEGREE_LEVEL/360,4);
        }

        //TODO: same shit
        this.angles[1] = guardVec;
        this.angles[2] = intruderVec;
    }

    public void getPherVec() {
        float[] redVec = new float[DEGREE_LEVEL];
        float[] greenVec = new float[DEGREE_LEVEL];
        float[] blueVec = new float[DEGREE_LEVEL];
        float[] yellowVec = new float[DEGREE_LEVEL];
        float[] purpleVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();

        for (Pheromone p : visiblePheromones) {
            Vector2 pherPos = p.getPosition();
            switch (p.getPheromoneType()) {
                case RED: redVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)*DEGREE_LEVEL/360] += 1; break;
                case BLUE: blueVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)*DEGREE_LEVEL/360] += 1; break;
                case GREEN: greenVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)*DEGREE_LEVEL/360] += 1; break;
                case YELLOW: yellowVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)*DEGREE_LEVEL/360] += 1; break;
                case PURPLE: purpleVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)*DEGREE_LEVEL/360] += 1; break;
            }
        }
        this.angles[3] = redVec;
        this.angles[4] = greenVec;
        this.angles[5] = blueVec;
        this.angles[6] = yellowVec;
    }

    public void getBorderVec() {
        Vector2[] bounds = agent.getVisibleBounds();
        float[] borderVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();

        if (bounds[0].x > -2) {
            //top bounds
            borderVec = distributedAngle(borderVec,50,DEGREE_LEVEL/4,40);
        }
        if (bounds[1].x > -2) {
            //bot bounds
            borderVec = distributedAngle(borderVec,50,DEGREE_LEVEL*3/4,40);
        }
        if (bounds[2].x > -2) {
            //right bounds
            borderVec = distributedAngle(borderVec,50,0,40);
        }
        if (bounds[3].x > -2) {
            //left bounds
            borderVec = distributedAngle(borderVec,50,DEGREE_LEVEL/2,40);
        }

        this.angles[8] = borderVec;
    }

    public void getAreaVec(){
        agent.updateVisibleAreas();
        float[] sentryVec = new float[DEGREE_LEVEL];
        float[] shadeVec = new float[DEGREE_LEVEL];
        float[] structVec = new float[DEGREE_LEVEL];

        //the variables need fixing
        for (Vector2[] v : agent.getVisibleSentryTowers()) {
            if (v[0].x > -2) {
                //top bounds
                sentryVec = distributedAngle(sentryVec,50,DEGREE_LEVEL/4,40);
            }
            if (v[1].x > -2) {
                //bot bounds
                sentryVec = distributedAngle(sentryVec,50,DEGREE_LEVEL*3/4,40);
            }
            if (v[2].x > -2) {
                //right bounds
                sentryVec = distributedAngle(sentryVec,50,0,40);
            }
            if (v[3].x > -2) {
                //left bounds
                sentryVec = distributedAngle(sentryVec,50,DEGREE_LEVEL/2,40);
            }
        }

        for (Vector2[] v : agent.getVisibleShade()) {
            if (v[0].x > -2) {
                //top bounds
                shadeVec = distributedAngle(shadeVec,50,DEGREE_LEVEL/4,40);
            }
            if (v[1].x > -2) {
                //bot bounds
                shadeVec = distributedAngle(shadeVec,50,DEGREE_LEVEL*3/4,40);
            }
            if (v[2].x > -2) {
                //right bounds
                shadeVec = distributedAngle(shadeVec,50,0,40);
            }
            if (v[3].x > -2) {
                //left bounds
                shadeVec = distributedAngle(shadeVec,50,DEGREE_LEVEL/2,40);
            }
        }

        for (Vector2[] v : agent.getVisibleStructure()) {
            if (v[0].x > -2) {
                //top bounds
                structVec = distributedAngle(structVec,50,DEGREE_LEVEL/4,40);
            }
            if (v[1].x > -2) {
                //bot bounds
                structVec = distributedAngle(structVec,50,DEGREE_LEVEL*3/4,40);
            }
            if (v[2].x > -2) {
                //right bounds
                structVec = distributedAngle(structVec,50,0,40);
            }
            if (v[3].x > -2) {
                //left bounds
                structVec = distributedAngle(structVec,50,DEGREE_LEVEL/2,40);
            }
        }

        this.angles[9] = sentryVec;
        this.angles[10] = shadeVec;
        this.angles[11] = structVec;

    }

    private float[] distributedAngle(float[] vec, double std, int pos,int importanceLevel) {
        std = std* DEGREE_LEVEL/360;
//        System.out.println("Visible agents angle position: " + pos);
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));

        float value = (float) (Math.exp(-1/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
        vec[pos] += value;
        if (pos+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
            vec[pos-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
        else
            vec[pos+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

        for(int i=1; i< importanceLevel*DEGREE_LEVEL/720; i++){
            value = (float) (Math.exp(-Math.pow(i,2)/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
            if (pos+i>=DEGREE_LEVEL)
                vec[pos+i-DEGREE_LEVEL] += value;
            else
                vec[pos+i] += value;

            if (pos-i<0)
                vec[pos-i+DEGREE_LEVEL] += value;
            else
                vec[pos-i] += value;

            if (pos+i+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
            else
                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

            if (pos+i-DEGREE_LEVEL/2 < 0)
                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
            else
                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

        }
//        System.out.println(Arrays.toString(vec));
//        System.out.println();
        return vec;
    }

    private float generateRandomNormal() {
        double r1 = Math.random();
        double r2 = Math.random();

        return (float)(Math.sqrt(-2*Math.log(r1))*Math.cos(2*Math.PI*r2));
    }

    private float euclideanDistance(Vector2 agent, Vector2 other) {
        return (float)Math.sqrt(Math.pow(agent.x - other.x,2)+Math.pow(agent.y - other.y,2));
    }

    private float getPositiveAngleBetweenTwoPos(Vector2 agent, Vector2 other)
    {
        return modulo(getAngleBetweenTwoPos(agent, other),360.0f);
        //    return modulo((float)Math.toDegrees(Math.atan((agent.y - other.y)/(agent.x - other.x))), 360.0f); //agent.x - other.x could be 0 and fail
    }

    public float getAngleBetweenTwoPos(Vector2 pos1, Vector2 pos2){

        return (float)(Math.toDegrees(Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x)));
    }

    public static float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }
}
