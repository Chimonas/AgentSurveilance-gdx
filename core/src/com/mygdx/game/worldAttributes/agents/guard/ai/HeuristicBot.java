package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

import java.util.LinkedList;
import java.util.Queue;

public class HeuristicBot extends GuardAI {


    //Have an array of all angles

    private Queue<float[]> angles;
    private float[] coefficients;
    private float[] best_actions;

    public HeuristicBot(Guard guard) {
        super(guard);
    }

    public void update() {
        super.update();
        this.best_actions = evaluateActions();

    }

    public float[] evaluateActions() {
        this.angles = new LinkedList();
        getSoundVec();
        getAgents(); // guards, intruders
        getPherVec(); // red, green, blue, yellow

        coefficients = new float[] {
                /* coefficient for sound: */ 5,
                /* coefficient for amount of visible guards: */ -5,
                /* coefficient for amount of visible intruders: */ 100,
                /* coefficient for red pheromones: */ 70,
                /* coefficient for green pheromones: */ 10,
                /* coefficient for blue pheromones: */ 10,
                /* coefficient for yellow pheromones: */ 10
        };

        float[] best_actions = new float[360];

        for (int i = 0; i < angles.size(); i++) {

            float[] values = angles.remove();
//            if (i == 1)
//                System.out.println(Arrays.toString(values));
            for (int j = 0; j < values.length; j++) {
                best_actions[j] += values[j] * coefficients[i];
            }

        }
//        System.out.println(Arrays.toString(best_actions));
        return best_actions;
    }

    @Override
    public float getNewVelocity() {
        return 1.4f;
    }

    @Override
    public float getNewAngle() {
        float max = -100;
        float angle = 0;
        for (int i = 0; i < best_actions.length; i++) {
            if (best_actions[i] >= max) {
                max = best_actions[i];
                angle = i;
                System.out.println("Max value " + max);
                System.out.println("angle " + i);
            }
        }
//        System.out.println("New angle " + angle);
//        System.out.println();

//        if (max == 0) {
//            angle = (float)Math.random() * 360 ;
//            System.out.println(angle);
//            while (best_actions[(int)Math.floor(angle)] != 0) {
//                angle = (float)Math.random() * 360 ;
//            }
//        }
        System.out.println(max);
        return angle ;
    }

    public void getSoundVec() {
        float[] soundVec = new float[360];

        Vector2 pos = agent.getPosition();
//        for (float f : visibleSounds)
//        {
//            soundVec[Math.round(getAngle(pos,soundPos))] += 1;
//        }

        //TODO: sounds are now floats of the direction they are from

        this.angles.add(soundVec);
//        return soundVec;
    }

    public void getPherVec() {
        float[] redVec = new float[360];
        float[] greenVec = new float[360];
        float[] blueVec = new float[360];
        float[] yellowVec = new float[360];

        Vector2 pos = agent.getPosition();

        for (Pheromone p : visiblePheromones) {
            Vector2 pherPos = p.getPosition();
            switch (p.getPheromoneType()) {
                case RED: redVec[Math.round(getAngle(pos,pherPos))] += 1;
                case BLUE: blueVec[Math.round(getAngle(pos,pherPos))] += 1;
                case GREEN: greenVec[Math.round(getAngle(pos,pherPos))] += 1;
                case YELLOW: yellowVec[Math.round(getAngle(pos,pherPos))] += 1;
            }
        }
        this.angles.add(redVec);
        this.angles.add(greenVec);
        this.angles.add(blueVec);
        this.angles.add(yellowVec);
    }

    public void getAgents() {
        float[] guardVec = new float[360];
        float[] intruderVec = new float[360];

        Vector2 pos = agent.getPosition();
        for (Agent a : visibleGuards) {
            Vector2 other = a.getPosition();
            guardVec = distributedAngle(guardVec,10,Math.round(getAngle(pos,other)));
//            guardVec[Math.round(getAngle(pos,other))] += 1;
        }
        for (Agent a : visibleIntruders) {
            Vector2 other = a.getPosition();
            agent.createPheromone(Pheromone.PheromoneType.RED);
            intruderVec[Math.round(getAngle(pos,other))] += 1;
        }

        this.angles.add(guardVec);
        this.angles.add(intruderVec);
    }

    private float[] distributedAngle(float[] vec, double std, int pos) {

        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));
        for(int i=0; i<45; i++){
            float value = (float) (Math.exp(-Math.pow(i,2)/(2*Math.pow(std,2)))/(Math.sqrt(2*Math.PI*Math.pow(std,2)))/scalar);
            if (pos+i>360)
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


    private float getAngle(Vector2 agent, Vector2 other)
    {
        return (float)Math.toDegrees(Math.atan((agent.y - other.y)/(agent.x - other.x))) + 180 ; //agent.x - other.x could be 0 and fail
    }

    private float euclideanDistance(Vector2 agent, Vector2 other) {
        return (float)Math.sqrt(Math.pow(agent.x - other.x,2)+Math.pow(agent.y - other.y,2));
    }
}
