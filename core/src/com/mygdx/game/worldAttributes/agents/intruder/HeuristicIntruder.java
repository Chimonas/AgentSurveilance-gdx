package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Structure;
import com.mygdx.game.worldAttributes.areas.Target;

import java.util.ArrayList;

public class HeuristicIntruder extends AI {

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

    private Vector2[] lastSeenIntruder = new Vector2[2];
    private int ticksFromLastSeenIntruder;

    public HeuristicIntruder(Agent agent, float[] coefficients) {
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


        if(this.oldPosition.equals(agent.position) && !check){

            samePositionTick = agent.getWorld().getGameLoop().getTicks();
            check= true;
        }

        if (agent.inSentryTower() && !insideStructure) {
            insideStructure = true;
            best_actions = new float[DEGREE_LEVEL];
        } else if (insideStructure && !agent.inSentryTower()) {
            insideStructure = false;
            newVelocity = agent.MAX_VELOCITY;
        }

        this.best_actions = evaluateActions();

        updateNewAngle(agent.getAngleFacing());
        this.oldPosition.set(agent.position);
    }

    @Override
    public void spawn(Map map)
    {

        Vector2 randomPosition = new Vector2();
        boolean isInside = false;
        do {
            randomPosition.x = (float) Math.random() * map.getWidth();
            randomPosition.y = (float) Math.random() * map.getHeight();

            isInside = false;
            for (Structure s : agent.getWorld().getMap().getStructures()) {
                if (s.contains(randomPosition)) {
                    isInside = true;
                    break;
                }
            }
        } while (isInside);

        agent.spawn(randomPosition, (float) Math.random() * 360.0f);

    }

    public float[] evaluateActions() {
        historyFade();
        getSoundVec();
        getAgents(); // guards, intruders
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

    public void updateNewAngle(float oldAngle) {
        float max = -100;
        float newAngle = oldAngle;
        for (int i = 0; i < best_actions.length; i++) {
            if (best_actions[i] >= max) {
                max = best_actions[i];
                newAngle = i;
            }
        }


        // 45/GameLoop.TICK_RATE

        if(check) {

            if(agent.position.x < 1.0f) newAngle = (float) (Math.random()*180 + 270)%360;
            else if(agent.position.y < 1.0f) newAngle = (float) (Math.random()*180);
            else if(agent.position.x > agent.getWorld().getMap().getWidth()- 1.0f) newAngle = (float) (Math.random()*180 + 90) ;
            else if(agent.position.y > agent.getWorld().getMap().getHeight() - 1.0f) newAngle = (float) (Math.random()*180 + 180);
            else newAngle = (float) (Math.random() * 360.0);

            samePositionTick = -1;
            check = false;
        }

        float angleDifference = angleDistance(oldAngle,newAngle);
//        if (Math.abs(angleDifference) > 45.0f / GameLoop.TICK_RATE) {
//                agent.ai.newVelocity = agent.MAX_VELOCITY;
//                newAngle = agent.angleFacing + directionToMove(newAngle) * 45.0f / (float) GameLoop.TICK_RATE;
//        }

        newAngle = newAngle*360/DEGREE_LEVEL;

        max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < 30; i++) {
            min = Math.min(min,best_actions[(int)modulo(i-1,360)]);
            min = Math.min(min,best_actions[(int)modulo(i+1,360)]);
            max = Math.max(max,best_actions[(int)modulo(i-1,360)]);
            max = Math.max(max,best_actions[(int)modulo(i+1,360)]);
        }

        System.out.println(max-min);

        agent.ai.newAngle = newAngle;
    }

    public void getSoundVec() {
        float[] soundVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();
        for (float f : visibleSounds)
        {
            soundVec[(int) f*DEGREE_LEVEL/360] += 1;
        }
//TODO: put order of coefficients in the constructor
        agent.createPheromone(Pheromone.PheromoneType.BLUE);
        this.angles[0] = soundVec;
    }

    public void getAgents() {
        float[] guardVec = new float[DEGREE_LEVEL];
        float[] intruderVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();
        int guards = 0;
        for (Agent a : visibleGuards) {
            guards++;
            if (guards == 2) {
                agent.createPheromone(Pheromone.PheromoneType.PURPLE);
            }
            Vector2 other = a.getPosition();
            guardVec = distributedAngle(guardVec,50, (int) getPositiveAngleBetweenTwoPos(pos,other)*DEGREE_LEVEL/360,100);
        }
        for (Agent a : visibleIntruders) {
            Vector2 other = a.getPosition();
            intruderVec = distributedAngle(intruderVec, 10, (int) getPositiveAngleBetweenTwoPos(pos, other)*DEGREE_LEVEL/360,40);

        }


        //TODO: same shit
        this.angles[1] = guardVec;
        this.angles[2] = intruderVec;
    }

    public void getBorderVec() {
        float[] borderVec = new float[DEGREE_LEVEL];

        Vector2 pos = agent.getPosition();

        borderVec = insideAreaEdit(borderVec,new Vector2(0,agent.world.getMap().getHeight()),
            new Vector2(agent.world.getMap().getWidth(),agent.world.getMap().getHeight()),
            new Vector2(0,0),
            new Vector2(agent.world.getMap().getWidth(),0));

        this.angles[3] = borderVec;
    }

    public void getAreaVec(){
        float[] shadeVec = new float[DEGREE_LEVEL];
        float[] structVec = new float[DEGREE_LEVEL];
        float[] insideSentryVec = new float[DEGREE_LEVEL];


        for (Shade s : agent.getVisibleShades()) {
            float[] angles = getMinMaxAngles(s);

            if (s.contains(agent.position)) {
                shadeVec = applyPull(shadeVec,s);
                shadeVec = applyDiagonalPulls(shadeVec,s);
            }
            else {

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

                corners = new float[]{val,val};

                shadeVec[(int) corners[0]] += 1;
                shadeVec[(int) corners[1]] += 1;
            }
        }


        for (Structure s : agent.getVisibleStructures()) {

            float[] minMax = getMinMaxAngles(s);

//                System.out.println("seeing a building");

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

            corners = new float[]{val,val};


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

            structVec[(int) corners[0]] += 1;
            structVec[(int) corners[1]] += 1;
        }

        this.angles[4] = shadeVec;
        this.angles[5] = structVec;
    }

    public void getTargetVec() {
        ArrayList<Target> targets = agent.world.getMap().getTargets();
        float[] targetVec = new float[DEGREE_LEVEL];

        for (Target t : targets) {
            float[] minMax = getMinMaxAngles(t);

            targetVec = distributedAngle(targetVec,20,(int)midAngle(minMax[0],minMax[1]),(int)(minMax[1]-minMax[0]));

        }

        this.angles[6] = targetVec;

    }

    private float[] distributedAngle(float[] vec, double std, int pos,int degreesToInfluence,float multiplier) {
        int importanceLevel = degreesToInfluence;
        std = std* DEGREE_LEVEL/360;
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));

        float value = (float) (Math.exp(-1/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar)* multiplier;
        vec[pos] += value;

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

        }
        return vec;
    }
    private float[] distributedAngle(float[] vec, double std, int pos,int degreesToInfluence) {
        int importanceLevel = degreesToInfluence;
        std = std* DEGREE_LEVEL/360;
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));

        float value = (float) (Math.exp(-1/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
        vec[pos] += value;

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

        }
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

        float normalizedBy = 1 /*Math.max(topLeft.x - topRight.x,topLeft.y - botLeft.y);*/;
        //        testing what happens when you do the reverse thing for the four corners
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,topRight.y))] += 1/agent.position.dst(new Vector2(botRight.x,topRight.y))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,topRight.y))] += 1/agent.position.dst(new Vector2(0,topRight.y))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,0))] += 1/agent.position.dst(new Vector2(0,0))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,0))] += 1/agent.position.dst(new Vector2(botRight.x,0))/normalizedBy;

        return vec;
    }

    private float[] applyDiagonalPush(float[] vec, Area a) {
        return applyDiagonalPulls(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight());
    }
    private float[] applyDiagonalPush(float[] vec, Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight) {

        float normalizedBy = Math.max(topLeft.x - topRight.x,topLeft.y - botLeft.y);
        //        testing what happens when you do the reverse thing for the four corners
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,topRight.y))] -= 1/agent.position.dst(new Vector2(botRight.x,topRight.y))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,topRight.y))] -= 1/agent.position.dst(new Vector2(0,topRight.y))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(0,0))] -= 1/agent.position.dst(new Vector2(0,0))/normalizedBy;
        vec[(int)getPositiveAngleBetweenTwoPos(agent.getPosition(),new Vector2(botRight.x,0))] -= 1/agent.position.dst(new Vector2(botRight.x,0))/normalizedBy;

        return vec;
    }


    private float[] applySideBombs(float[] vec, Area a) {
        return applySideBombs(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight());
    }
    private float[] applySideBombs(float[] vec, Area a, int angleToInfluence, int degreesToInfluence,float multiplier) {
        return applySideBombs(vec,a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight(),angleToInfluence,degreesToInfluence,multiplier);

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
    private float[] applySideBombs(float[] vec,Vector2 topLeft, Vector2 topRight, Vector2 botLeft, Vector2 botRight,int angleToInfluence,int degreesToInfluence,float multiplier) {
        int std = 50;


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

    private float[] applyPull(float[] vec, Area a) {
        float horizontal = (a.getTopRight().x - agent.position.x) - (agent.position.x - a.getTopLeft().x);
        float vertical = (a.getTopRight().y - agent.position.y) - (agent.position.y - a.getBottomRight().y);

        float right = a.getTopRight().x - agent.position.x;
        float up = a.getTopRight().y - agent.position.y;
        float left = agent.position.x - a.getTopLeft().x;
        float down = agent.position.y - a.getBottomLeft().y;

        vec = distributedAngle(vec,40,0,90, 1/right);
        vec = distributedAngle(vec,40,DEGREE_LEVEL/4,90,1/up);
        vec = distributedAngle(vec,40,DEGREE_LEVEL/2,90,1/left);
        vec = distributedAngle(vec,40,DEGREE_LEVEL*3/4,90,1/down);


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

    private float[] getMidAnglePoints(Area a) {
        Vector2[] edges = new Vector2[] {a.getTopLeft(),a.getTopRight(),a.getBottomLeft(),a.getBottomRight()};
        Vector2[] sides = new Vector2[]{ new Vector2(edges[0].x, (edges[0].y + edges[2].y)/2),
            new Vector2(edges[1].x, (edges[1].y + edges[3].y)/2),
            new Vector2((edges[0].x + edges[1].x)/2,edges[0].y),
            new Vector2((edges[2].x + edges[3].x)/2,edges[2].y) };

        float distance = 200;
        float angle = 0;
        for (Vector2 s : sides) {
            float dst = agent.position.dst(s);
            if (dst < distance) {
                distance = dst;
                angle = getPositiveAngleBetweenTwoPos(agent.position,s);
            }
        }



        return new float[]{angle,distance};
    }

    public boolean isInExtension(Area a) {
        return (agent.position.x > a.getTopRight().x || agent.position.x < a.getTopLeft().x) &&
            (agent.position.y > a.getTopRight().y || agent.position.y < a.getBottomLeft().y);
    }

    private float directionToMove(float to) {
        float from = agent.getAngleFacing();
        if (from < 360 && from > 270 && to < 90) {
            return +1;
        } else if (to < 360 && to > 270 && from < 90) {
            return -1;
        } else {
            return (to-from)/(to-from);
        }

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
