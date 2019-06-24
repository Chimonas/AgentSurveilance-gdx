package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

import java.util.ArrayList;

public class GeneticAlgo {

    int numberSimulationsPerIteration = 100;
    int numberCoefficients;
    float[] coefficients;
    World world;
    public ArrayList<AStarAI.Pair<Guard,Double>> guardWinners;
    public ArrayList<AStarAI.Pair<Intruder,Double>> intruderWinners;
    public ArrayList<Guard> guards;
    public ArrayList<Guard> newGuards;
    public ArrayList<Intruder> intruders;
    public ArrayList<Intruder> newIntruders;

    public int numberOfCrossovers = (int) Math.floor(guards.size()/2);
    public float mutationProbability = 0.1f;


    public GeneticAlgo(ArrayList<Guard> guards, ArrayList<Intruder> intruders){

        world = guards.get(0).world;
        this.guards = guards;
        this.intruders = intruders;

    }

    public void startSimulation(){

        for(Guard g: guards){
            g.setSimulationAI(GuardAI.GuardAIType.SWARM_HEURISTIC);
            ((HeuristicBot) g.ai).setCoefficients(
                    generateRandomCoefficients(((HeuristicBot) g.ai).getCoefficients().length));
            g.spawn();
        }
        for(Intruder i: intruders){
            i.setSimulationAI(IntruderAI.IntruderAIType.A_STAR);
            i.spawn();
        }
    }

    //Not generic enough..... just applies to Guard but should be generic enough
    public void updateAgents(){
        guardWinners = this.world.getGameLoop().guardWinners;
        double firstBestMetric = 10000000;
        double secondBestMetric = 10000000;
        Agent firstBestGuard = new Guard(this.world);
        Agent secondBestGuard = new Guard(this.world);

        for(AStarAI.Pair<Guard, Double> guard: guardWinners)
            if(guard.getRight() < firstBestMetric) {
                secondBestMetric = firstBestMetric;
                firstBestMetric = guard.getRight();
                secondBestGuard = firstBestGuard;
                firstBestGuard = guard.getLeft();
            }

        for(int i=0; i<numberOfCrossovers; i++){
            Agent newAgent = crossOver(firstBestGuard, secondBestGuard);
            newGuards.add((Guard)newAgent);
        }
    }

    public Agent crossOver(Agent a, Agent b){

        float alpha = (float) (Math.random()*2.0 - 0.5);
        float[] newCoeff = new float[numberCoefficients];

        for(int i=0; i < numberCoefficients; i++){
            float coefA = ((HeuristicBot)a.ai).getCoefficients()[i];
            float coefB = ((HeuristicBot)b.ai).getCoefficients()[i];
            newCoeff[i] = alpha*coefA + (1-alpha)*coefB;
        }

        Agent newAgent;
        if(a instanceof Guard) {
            newAgent = new Guard(this.world);
            ((Guard) newAgent).setSimulationAI(GuardAI.GuardAIType.SWARM_HEURISTIC);
            ((HeuristicBot)newAgent.ai).setCoefficients(newCoeff);
        } else {
            newAgent = new Intruder(this.world);
            ((Intruder) newAgent).setSimulationAI(IntruderAI.IntruderAIType.SWARM_HEURISTIC);
            ((HeuristicBot)newAgent.ai).setCoefficients(newCoeff);
        }

        return newAgent;
    }


    public void mutation(){

    }

    public float[] generateRandomCoefficients(int length){

        float [] coeff = new float[length];
        for(int i = 0; i < length; i++)
            coeff[0] = (float) (Math.random() * 200.0f - 100.0f);

        return coeff;
    }

    public void setInitialCoefficients(float[] c){
        coefficients = c;
    }


}
