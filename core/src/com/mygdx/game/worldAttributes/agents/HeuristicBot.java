package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;

import java.util.LinkedList;
import java.util.Queue;

public class HeuristicBot extends AI {

    //Have an array of all angles

    private Queue<float[]> angles;
    private float[] coefficients;
    private float[] best_actions;
    PheromoneAI pherAI;

    public HeuristicBot(Agent agent) {
        super(agent);
        this.pherAI = new PheromoneAI(agent);
    }

    public void update() {
        super.update();
        this.pherAI.update();

        //HERE THE MEANING FOR EACH PHEROMONE IS SET
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.RED, PheromoneAI.PheromoneAction.FOLLOWINTRUDER);
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.BLUE, PheromoneAI.PheromoneAction.HEARDSOUND);
        this.pherAI.setPheromoneToAction(Pheromone.PheromoneType.GREEN, PheromoneAI.PheromoneAction.FOUNDSHADE);

        this.best_actions = evaluateActions();

    }

    public float[] evaluateActions() {
        this.angles = new LinkedList();
        getSoundVec();
        getAgents(); // guards, intruders
        getPherVec(); // red, green, blue, yellow

        coefficients = new float[] {
                /* coefficient for sound: */ 0, //SOUND REALLY FUCKS UP GUARDS
                /* coefficient for amount of visible guards: */ -20,
                /* coefficient for amount of visible intruders: */ 10,
                /* coefficient for red pheromones: */ 10,
                /* coefficient for green pheromones: */ 10,
                /* coefficient for blue pheromones: */ 10,
                /* coefficient for yellow pheromones: */ 10,
                /* coefficient for purple pheromones: */ 10,
        };

        float[] best_actions = new float[360];

        for (int i = 0; i < angles.size(); i++) {

            float[] values = angles.remove();
            for (int j = 0; j < values.length; j++)
                best_actions[j] += values[j] * coefficients[i];

        }

        return best_actions;
    }

    @Override
    public float getNewVelocity() {
        return 1.4f;
    }

    @Override
    public void spawn(Map map) {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        agent.spawnPosition(randomPosition, (float)Math.random() * 360.0f);
    }

    @Override
    public float getNewAngle(float oldAngle) {
        float max = -100;
        float angle = oldAngle;
        for (int i = 0; i < best_actions.length; i++) {
            if (best_actions[i] >= max) {
                max = best_actions[i];
                angle = i;
            }
        }

        if(best_actions[(int)angle] <= best_actions[(int)oldAngle])
            angle = oldAngle;

        return angle ;
    }

    public void getSoundVec() {
        float[] soundVec = new float[360];

        Vector2 pos = agent.getPosition();
        for (float f : visibleSounds)
        {
            soundVec[(int) f] += 1;
        }

        this.angles.add(soundVec);
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
                case RED: redVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1;
                case BLUE: blueVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1;
                case GREEN: greenVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1;
                case YELLOW: yellowVec[(int) getPositiveAngleBetweenTwoPos(pos, pherPos)] += 1;
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
            guardVec = distributedAngle(guardVec,10, (int) getPositiveAngleBetweenTwoPos(other,pos));
        }
        for (Agent a : visibleIntruders) {
            Vector2 other = a.getPosition();
            intruderVec = distributedAngle(intruderVec, 10, (int) getPositiveAngleBetweenTwoPos(other, pos));
        }

        this.angles.add(guardVec);
        this.angles.add(intruderVec);
    }

    private float[] distributedAngle(float[] vec, double std, int pos) {

//        System.out.println("Visible agents angle position: " + pos);
        double scalar = 1/(Math.sqrt(2*Math.PI*Math.pow(std,2)));
        for(int i=0; i<90; i++){
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

        return (float)(Math.toDegrees(Math.atan2(pos1.y - pos2.y, pos1.x - pos2.x)));
    }

    public static float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }
}
