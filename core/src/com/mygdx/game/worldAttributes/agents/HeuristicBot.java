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
    private float[] best_actions = new float[360];
    public Vector2 oldPosition = new Vector2();
    PheromoneAI pherAI;

    private int samePositionTick = -1;
    boolean check = false;
    private float maxTimeSamePosition = 3.0f;

    public HeuristicBot(Agent agent) {
        super(agent);
        this.pherAI = new PheromoneAI(agent);
        this.newVelocity= agent.MAX_VELOCITY;

        //this.numberCoefficients = 0;
        for(int i = 0; i<best_actions.length; i++) best_actions[i] = (float)Math.random();
//        best_actions[(int)Math.random()*360] = 0.1f;

        coefficients = new float[] {
                /* coefficient for sound: */ 0, //SOUND REALLY FUCKS UP GUARDS
                /* coefficient for amount of visible guards: */ -1.5f,
                /* coefficient for amount of visible intruders: */ 5,
                /* coefficient for red pheromones: */ 2,
                /* coefficient for green pheromones: */ 0,
                /* coefficient for blue pheromones: */ 0.2f,
                /* coefficient for yellow pheromones: */ 0,
                /* coefficient for purple pheromones: */ 0,
                /* coefficient for map border: */ -1,
        };

        this.angles = new float[coefficients.length][360];

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


        for (int i = 0; i < angles.length; i++)
            for (int j = 0; j < angles[i].length; j++)
                best_actions[j] += angles[i][j] * coefficients[i];
//        System.out.println(Arrays.toString(best_actions));
        return best_actions;
    }
//
//    public float[][] historyFade2() {
//        float[][] newAngles = angles;
//        //new float[360][numberCoefficients];
//
//        for (int i = 0; i < newAngles.length; i++)
//            for (int j = 0; j < newAngles[i].length; j++)
//                newAngles[i][j] *= 0.95;
//
//        System.out.println("angle: " + Arrays.deepToString(newAngles));
//        return newAngles;
//    }

    public void historyFade() {

        //new float[360][numberCoefficients];

        for (int i = 0; i < best_actions.length; i++) {
            float value = best_actions[i];
            best_actions[i] *= 0.9;
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

        newAngle = angle;
    }

    public void getSoundVec() {
        float[] soundVec = new float[360];

        Vector2 pos = agent.getPosition();
        for (float f : visibleSounds)
        {
            soundVec[(int) f] += 1;
        }
       //TODO: put order of coefficients in the constructor
        this.angles[0] = soundVec;
    }

    public void getPherVec() {
        float[] redVec = new float[360];
        float[] greenVec = new float[360];
        float[] blueVec = new float[360];
        float[] yellowVec = new float[360];
        float[] purpleVec = new float[360];

        Vector2 pos = agent.getPosition();

        for (Pheromone p : visiblePheromones) {
            Vector2 pherPos = p.getPosition();
            switch (p.getPheromoneType()) {
                case RED: redVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1; break;
                case BLUE: blueVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1; break;
                case GREEN: greenVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1; break;
                case YELLOW: yellowVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1; break;
                case PURPLE: purpleVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1; break;
            }
        }
        this.angles[3] = redVec;
        this.angles[4] = greenVec;
        this.angles[5] = blueVec;
        this.angles[6] = yellowVec;
    }

    public void getBorderVec() {
        Vector2[] bounds = agent.getVisibleBounds();
        float[] borderVec = new float[360];

        Vector2 pos = agent.getPosition();

        if (bounds[0].x > -2) {
            //top bounds
//            float lambdaPoisson = (float) (Math.sin(getPositiveAngleBetweenTwoPos(pos,bounds[0]))/2);
//            borderVec = distributedAnglePoisson(borderVec,lambdaPoisson,(int)getPositiveAngleBetweenTwoPos(pos,bounds[0]));
            borderVec = distributedAngle(borderVec,50,90);
        }
        if (bounds[1].x > -2) {
            //bot bounds
//            float lambdaPoisson = (float) (Math.sin(getPositiveAngleBetweenTwoPos(pos,bounds[1]))/2);
//            borderVec = distributedAnglePoisson(borderVec,lambdaPoisson,(int)getPositiveAngleBetweenTwoPos(pos,bounds[1]));
            borderVec = distributedAngle(borderVec,50,270);
        }
        if (bounds[2].x > -2) {
            //right bounds
//            float lambdaPoisson = (float) (Math.cos(getPositiveAngleBetweenTwoPos(pos,bounds[2]))/2);
//            borderVec = distributedAnglePoisson(borderVec,lambdaPoisson,(int)getPositiveAngleBetweenTwoPos(pos,bounds[2]));
            borderVec = distributedAngle(borderVec,50,0);
        }
        if (bounds[3].x > -2) {
            //left bounds
//            float lambdaPoisson = (float) (Math.cos(getPositiveAngleBetweenTwoPos(pos,bounds[3]))/2);
//            borderVec = distributedAnglePoisson(borderVec,lambdaPoisson,(int)getPositiveAngleBetweenTwoPos(pos,bounds[3]));
            borderVec = distributedAngle(borderVec,50,180);
        }

        this.angles[8] = borderVec;
    }

    public void getAgents() {
        float[] guardVec = new float[360];
        float[] intruderVec = new float[360];

        Vector2 pos = agent.getPosition();
        for (Agent a : visibleGuards) {
            Vector2 other = a.getPosition();
            guardVec = distributedAngle(guardVec,10, (int) getPositiveAngleBetweenTwoPos(pos,other));
        }
        for (Agent a : visibleIntruders) {
            Vector2 other = a.getPosition();
            intruderVec = distributedAngle(intruderVec, 10, (int) getPositiveAngleBetweenTwoPos(pos, other));
        }

        //TODO: same shit
        this.angles[1] = guardVec;
        this.angles[2] = intruderVec;
    }

    private static float[] distributedAnglePoisson(float[] vec, float percentage,int pos) {
        int k = 120;
        float[] vecToUpdate = new float[k];
        percentage = Math.abs(percentage);
        int startAngle = (int)(pos - percentage * k);
        percentage = 1 - percentage;

        for (int i = 0; i < k; i++) {
            vecToUpdate[i] = (float)Math.pow(percentage,Math.abs(pos - startAngle - i));
        }
        for (int i = startAngle; i < startAngle + k; i++) {
            if (i < 0)
                vec[i+360] += vecToUpdate[i-startAngle];
            else if (i > 359)
                vec[i-360] += vecToUpdate[i-startAngle];
            else vec[i] += vecToUpdate[i-startAngle];
        }

        return vec;
    }

    private float[] distributedAngle(float[] vec, double std, int pos) {

//        System.out.println("Visible agents angle position: " + pos);
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));
        for(int i=0; i<100; i++){
            float value = (float) (Math.exp(-Math.pow(i,2)/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
            if (pos+i>=360)
                vec[pos+i-360] += value;
            else
                vec[pos+i] += value;

            if (pos-i<0)
                vec[pos-i+360] += value;
            else
                vec[pos-i] += value;

        }
        return vec;
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
