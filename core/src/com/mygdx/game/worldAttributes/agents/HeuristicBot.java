package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.SentryTower;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Structure;

import java.util.Arrays;

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

    private boolean insideStructure;
    private Structure inside;

    private float imaginaryAreaIncrease;

    public HeuristicBot(Agent agent, float[] coefficients) {
        super(agent);
        this.pherAI = new PheromoneAI(agent);
        this.newVelocity= agent.MAX_VELOCITY;
        this.coefficients = coefficients;
        for(int i = 0; i<best_actions.length; i++) best_actions[i] = (float)Math.random()*5;
//        best_actions[(int)Math.random()*360] = 0.1f;


        this.angles = new float[coefficients.length][DEGREE_LEVEL];

        imaginaryAreaIncrease =  (float)Math.sin(Math.toRadians(22.5)) * agent.visibility;


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

    @Override
    public void spawn(Map map) {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        agent.spawn(randomPosition, (float)Math.random() * 360.0f);
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

        if(check) {

            if(agent.position.x < 1.0f) angle = (float) (Math.random()*180 + 270)%360;
            else if(agent.position.y < 1.0f) angle = (float) (Math.random()*180);
            else if(agent.position.x > agent.getWorld().getMap().getWidth()- 1.0f) angle = (float) (Math.random()*180 + 90) ;
            else if(agent.position.y > agent.getWorld().getMap().getHeight() - 1.0f) angle = (float) (Math.random()*180 + 180);
            else angle = (float) (Math.random() * 360.0);


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
            guardVec = distributedAngle(guardVec,30, (int) getPositiveAngleBetweenTwoPos(pos,other)*DEGREE_LEVEL/360,100);
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
        float[] borderVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();

        borderVec = insideAreaEdit(borderVec,new Vector2(0,agent.world.getMap().getHeight()),
                new Vector2(agent.world.getMap().getWidth(),agent.world.getMap().getHeight()),
                new Vector2(0,0),
                new Vector2(agent.world.getMap().getWidth(),0));

        this.angles[8] = borderVec;
    }

    public void getAreaVec(){
        float[] sentryVec = new float[DEGREE_LEVEL];
        float[] shadeVec = new float[DEGREE_LEVEL];
        float[] structVec = new float[DEGREE_LEVEL];
        float[] insideStructVec = new float[DEGREE_LEVEL];

        //the variables need fixing
//        for (SentryTower s : agent.getVisibleSentryTowers()) {
//            float[] angles = getMinMidMaxAngles(s);
//
//            sentryVec = distributedAngle(sentryVec,50,(int)angles[1],(int)Math.abs(angles[0]-angles[1]));
//        }
//
//        for (Shade s : agent.getVisibleShades()) {
//            float[] angles = getMinMidMaxAngles(s);
//
//            shadeVec = distributedAngle(shadeVec,50,(int)angles[1],(int)Math.abs(angles[0]-angles[1]));
//        }


        if (insideStructure) {
            insideStructVec = insideAreaEdit(insideStructVec,inside);
        } else {
            for (Structure s : agent.world.getMap().getStructures()) {

                float[] minMax = getMinMaxAngles(s);


                //applying corner pulling
                float[] corners = new float[]{agent.getAngleFacing(),0};
                float val = 0;
                if (Math.min(Math.abs(corners[0] - minMax[0]), Math.abs(corners[0] - minMax[0] - 360))
                        < Math.min(Math.abs(corners[0] - minMax[1]), Math.abs(corners[0] - minMax[1] - 360))) {

                    corners = getAnglesOfImaginaryArea(s,minMax[0]);
                    val = minMax[0];
                } else {
                    corners = getAnglesOfImaginaryArea(s,minMax[1]);
                    val = minMax[1];
                }

//                corners = new float[]{val,val};

                structVec[(int) corners[0]] += 1;
                structVec[(int) corners[1]] += 1;


                //applying side bombs
                minMax[0] += 45;
                float angleToInfluence;
                boolean modifiablePos = false;
                if (minMax[0] > 360 && minMax[1] < 50) {
                    minMax[0] = modulo(minMax[0],360);
                    modifiablePos = minMax[1] > minMax[0];
                } else if (minMax[0] > 360){
                    modifiablePos = minMax[1] > minMax[0];
                    minMax[0] = modulo(minMax[0],360);
                } else {
                    modifiablePos = minMax[1] > minMax[0];
                }

                if (modifiablePos) {
                    angleToInfluence = midAngle(minMax[0],minMax[1]);
                    structVec = applySideBombs(structVec,s,(int)angleToInfluence,(int)angleDistance(minMax[0],minMax[1]),isInExtension(s));
                }

//                System.out.println(Arrays.toString(minMax));
                //multiplier must be small when the side that we are facing is small

            }
        }

        this.angles[9] = sentryVec;
        this.angles[10] = shadeVec;
        this.angles[11] = structVec;
        this.angles[12] = insideStructVec;

    }

    private float[] distributedAngle(float[] vec, double std, int pos,int degreesToInfluence,float multiplier) {
        int importanceLevel = degreesToInfluence;
        std = std* DEGREE_LEVEL/360;
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));

        float value = (float) (Math.exp(-1/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar)* multiplier;
        vec[pos] += value;
//        if (pos+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
//            vec[pos-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//        else
//            vec[pos+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

        for(int i=1; i< importanceLevel*DEGREE_LEVEL/720; i++){
            value = (float) (Math.exp(-Math.pow(i,2)/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))* multiplier/scalar);
            if (pos+i>=DEGREE_LEVEL)
                vec[pos+i-DEGREE_LEVEL] += value;
            else
                vec[pos+i] += value;

            if (pos-i<0)
                vec[pos-i+DEGREE_LEVEL] += value;
            else
                vec[pos-i] += value;

//            if (pos+i+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
//                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//            else
//                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//
//            if (pos+i-DEGREE_LEVEL/2 < 0)
//                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//            else
//                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

        }
//        System.out.println(Arrays.toString(vec));
//        int counter = 0;
//        for (int i = 0; i < vec.length; i++)
//            if (vec[i] != 0)
//                counter ++;
//        System.out.println(counter);
//
//        System.out.println(vec[0]);
//        System.out.println(vec[90]);
//        System.out.println(vec[180]);
//        System.out.println(vec[270]);
//
//        System.out.println();
        return vec;
    }
    private float[] distributedAngle(float[] vec, double std, int pos,int degreesToInfluence) {
        int importanceLevel = degreesToInfluence;
        std = std* DEGREE_LEVEL/360;
//        System.out.println("Visible agents angle position: " + pos);
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));

        float value = (float) (Math.exp(-1/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
        vec[pos] += value;
//        if (pos+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
//            vec[pos-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//        else
//            vec[pos+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

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

//            if (pos+i+DEGREE_LEVEL/2 >= DEGREE_LEVEL)
//                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//            else
//                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//
//            if (pos+i-DEGREE_LEVEL/2 < 0)
//                vec[pos+i+DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;
//            else
//                vec[pos+i-DEGREE_LEVEL/2] -= generateRandomNormal()*3/4;

        }
//        System.out.println(Arrays.toString(vec));
//        System.out.println();
        return vec;
    }

    private float[] insideAreaEdit(float[] vec, Area a) {
        return insideAreaEdit(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight());
    }
    private float[] insideAreaEdit(float[] vec, Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight) {

        return applyDiagonalPulls(applySideBombs(vec,topLeft,topRight,botLeft,botRight),topLeft,topRight,botLeft,botRight);
    }

    private float[] applyDiagonalPulls(float[] vec, Area a) {
        return applyDiagonalPulls(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight());
    }
    private float[] applyDiagonalPulls(float[] vec, Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight) {
        //        testing what happens when you do the reverse thing for the four corners
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,topRight.y))] += 1/agent.position.dst(new Vector2(botRight.x,topRight.y));
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,topRight.y))] += 1/agent.position.dst(new Vector2(0,topRight.y));
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,0))] += 1/agent.position.dst(new Vector2(0,0));
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,0))] += 1/agent.position.dst(new Vector2(botRight.x,0));

        return vec;
    }

    private float[] applySideBombs(float[] vec, Area a) {
        return applySideBombs(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight());
    }
    private float[] applySideBombs(float[] vec,Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight) {
        float multiplier = 1;
        int std = 50;
        int degreesToInfluence = 180;

        Vector2 pos = agent.position;

        if (pos.dst2(new Vector2(botRight.x, pos.y)) < agent.visibility * agent.visibility) {
            vec = distributedAngle(vec,std,0,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(pos.x,topRight.y)) < agent.visibility * agent.visibility) {
            vec = distributedAngle(vec,std,90,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(0,pos.y)) < agent.visibility * agent.visibility) {
            vec = distributedAngle(vec,std,180,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(pos.x,0)) < agent.visibility * agent.visibility) {
            vec = distributedAngle(vec,std,270,degreesToInfluence,-multiplier);
        }
        return vec;
    }
    private float[] applySideBombs(float[] vec, Area a, int angleToInfluence,int degreesToInfluence,boolean inExtension) {
        return applySideBombs(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight(),angleToInfluence,degreesToInfluence,inExtension);
    }
    private float[] applySideBombs(float[] vec,Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight,int angleToInfluence,int degreesToInfluence, boolean inExtension) {
        float multiplier = 1;
        int std = 50;

        Vector2 pos = agent.position;

        if (pos.dst2(new Vector2(botRight.x, pos.y)) < agent.visibility * agent.visibility) {
            if (pos.x < botLeft.x && inExtension)  vec = distributedAngle(vec,std,angleToInfluence,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(pos.x,topRight.y)) < agent.visibility * agent.visibility) {
            if (pos.y < botLeft.y && inExtension)  vec = distributedAngle(vec,std,angleToInfluence,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(0,pos.y)) < agent.visibility * agent.visibility) {
            if (pos.x > botRight.x && inExtension)  vec = distributedAngle(vec,std,angleToInfluence,degreesToInfluence,-multiplier);
        }

        if (pos.dst2(new Vector2(pos.x,0)) < agent.visibility * agent.visibility) {
            if (pos.y > topLeft.y && inExtension)  vec = distributedAngle(vec,std,angleToInfluence,degreesToInfluence,-multiplier);
        }
        return vec;
    }

    public float[] getAnglesOfImaginaryArea(Area a, float angle) {
        Vector2[] edges = new Vector2[]{a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight()};
        Vector2 pos = agent.position;
        int index = 0;
        int i = 0;
        Vector2 editPos = new Vector2();
        float[] result = new float[2];

        for (Vector2 e : edges) {
//            System.out.println(getPositiveAngleBetweenTwoPos(pos,e));
            if (Math.abs(getPositiveAngleBetweenTwoPos(pos,e) - angle) < Math.pow(10,-2)) {
                index = i;
                editPos = new Vector2(e.x,e.y);
                break;
            }
            i++;
        }

        if (index == 0 || index == 2) {
            //at the left of the structure
            result[0] = getPositiveAngleBetweenTwoPos(pos,editPos.add(-imaginaryAreaIncrease,0));
        } else {
            result[0] = getPositiveAngleBetweenTwoPos(pos,editPos.add(imaginaryAreaIncrease,0));
        }

        if (index < 2) {
            // above structure
            result[1] = getPositiveAngleBetweenTwoPos(pos,editPos.add(0,imaginaryAreaIncrease));
        } else {
            result[1] = getPositiveAngleBetweenTwoPos(pos,editPos.add(0,-imaginaryAreaIncrease));
        }

        return result;
    }

    private float generateRandomNormal() {
        double r1 = Math.random();
        double r2 = Math.random();

        return (float)(Math.sqrt(-2*Math.log(r1))*Math.cos(2*Math.PI*r2));
    }

    private float euclideanDistance(Vector2 agent, Vector2 other) {
        return (float)Math.sqrt(Math.pow(agent.x - other.x,2)+Math.pow(agent.y - other.y,2));
    }

    private float getPositiveAngleBetweenTwoPos(Vector2 agent, Vector2 other) {
        return modulo(getAngleBetweenTwoPos(agent, other),360.0f);
        //    return modulo((float)Math.toDegrees(Math.atan((agent.y - other.y)/(agent.x - other.x))), 360.0f); //agent.x - other.x could be 0 and fail
    }

    public float getAngleBetweenTwoPos(Vector2 pos1, Vector2 pos2) {

        return (float)(Math.toDegrees(Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x)));
    }

    private float[][] getDistancesAndAngles(Area a) {

        float dst1 = agent.position.dst(a.getBottomLeft());
        float ang1 = modulo(getAngleBetweenTwoPos(agent.position,a.getBottomLeft()),360);
        float dst2 = agent.position.dst(a.getBottomRight());
        float ang2 = modulo(getAngleBetweenTwoPos(agent.position,a.getBottomRight()),360);
        float dst3 = agent.position.dst(a.getTopLeft());
        float ang3 = modulo(getAngleBetweenTwoPos(agent.position,a.getTopLeft()),360);
        float dst4 = agent.position.dst(a.getTopRight());
        float ang4 = modulo(getAngleBetweenTwoPos(agent.position,a.getTopRight()),360);
        float[][] res = new float[][]{{ang1,dst1},{ang2,dst2},{ang3,dst3},{ang4,dst4}};
        return res;
    }

    private float[] getMinMaxAngles(Area a) {
        Vector2[] edges = new Vector2[] {a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight()};

        float[] minMaxAngle = new float[]{370, -200};
        float distance = -1;

        for (int i = 0; i < edges.length-1; i++)
        {
            float angle1 = getPositiveAngleBetweenTwoPos(agent.getPosition(),edges[i]);

            for (int j = i + 1; j < edges.length; j++) {
                float angle2 = getPositiveAngleBetweenTwoPos(agent.getPosition(),edges[j]);
                if (distance < angleDistance(angle1,angle2)) {
                    distance = angleDistance(angle1,angle2);

                    if (angle1 > 180 ^ angle2 > 180) {
                        minMaxAngle[0] = angle1;
                        minMaxAngle[1] = angle2;
                    } else {
                        minMaxAngle[0] = angle2;
                        minMaxAngle[1] = angle1;
                    }
                }

            }

        }

        minMaxAngle[0] = modulo(minMaxAngle[0],360);
        minMaxAngle[1] = modulo(minMaxAngle[1],360);


        return minMaxAngle;
    }

    private float angleDistance(float angle1, float angle2) {
        if ((angle1 < 360 && angle1 > 270 && angle2 < 180) ^ (angle2 < 360 && angle2 > 270 && angle1 < 180)) {
            return Math.min(modulo(angle1-angle2,360),modulo(angle2-angle1,360));
        } else {
            return Math.abs(angle1 - angle2);
        }
    }

    private float midAngle(float angle1, float angle2) {
        if ((angle1 < 360 && angle1 > 270 && angle2 < 180) ^ (angle2 < 360 && angle2 > 270 && angle1 < 180)) {
            if (angle1 > 270) {
                angle1 -= 360;
            } else {
                angle2 -= 360;
            }
        }
        return (angle1 + angle2)/2;
    }

    public boolean isInExtension(Area a) {
        return (agent.position.x > a.getTopRight().x || agent.position.x < a.getTopLeft().x) &&
                (agent.position.y > a.getTopRight().y || agent.position.y < a.getBottomLeft().y);
    }

    public static float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }

    public void setCoefficients(float[] c){
        this.coefficients = c;
    }
    public float[] getCoefficients(){
        return coefficients;
    }

}
