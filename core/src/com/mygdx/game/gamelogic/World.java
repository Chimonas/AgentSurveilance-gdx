package com.mygdx.game.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.StateManager;
import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.AStarAI;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.GeneticAlgo;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

import java.util.ArrayList;

public class World
{
    private static final float RANDOM_SOUND_VISIBILITY = 5.0f;

    private int simulationStartTick;

    private Map map;
    private GameLoop gameLoop;
    private Settings settings;
    private ArrayList<Guard> guards;
    private ArrayList<Intruder> intruders;
    private ArrayList<Communication> communications, bufferedCommunications;
    private ArrayList<Sound> sounds, bufferedSounds;
    private ArrayList<Pheromone> pheromones;

    //Olgas stuff
    ArrayList<Vector2> aStarGraphNodes;
    ArrayList<AStarAI.Pair<Vector2, Vector2>> aStarGraphEdges;
    GeneticAlgo geneticAlgo;

    public World(Map map, Settings settings)
    {
        this.map = map;
        this.settings = settings;
        gameLoop = new GameLoop(this, settings.isMultipleSimulations(), settings.getMaxTime(), settings.isExplorationPhase(), settings.getExplorationTime());

        guards = new ArrayList<>();
        intruders = new ArrayList<>();
        communications = new ArrayList<>();
        bufferedCommunications = new ArrayList<>();
        sounds = new ArrayList<>();
        bufferedSounds = new ArrayList<>();
        pheromones = new ArrayList<>();
        aStarGraphNodes = new ArrayList<>();
        aStarGraphEdges = new ArrayList<>();
       // geneticAlgo = new GeneticAlgo(guards, intruders);

        for (int i = 0; i < this.settings.getGuardAmount(); i++)
        {
            Guard newGuard = new Guard(this);
            guards.add(newGuard);
        }

        for (int i = 0; i < this.settings.getIntruderAmount(); i++)
        {
            Intruder newIntruder = new Intruder(this);
            intruders.add(newIntruder);
        }

        gameLoop.start();
    }

    public void startExplorationPhase()
    {
        for(Guard guard : guards)
        {
            guard.setExplorationAI(settings.getExplorationAIType());
            guard.aiSpawn();
        }
    }

    public void startSimulationPhase()
    {
        simulationStartTick = gameLoop.getTicks();
        if(this.getGameLoop().geneticAlgo)
            geneticAlgo.startSimulation();
        else {
            for (Guard guard : guards) {
                guard.setSimulationAI(settings.getGuardAIType());
                guard.aiSpawn();
            }

            for (Intruder intruder : intruders) {
                intruder.setIntruderAI(settings.getIntruderAIType());
                intruder.aiSpawn();
            }
        }
        pheromones.clear();
    }

    public void update()
    {
        //Clear communications of old update cycle and add new communications of current cycle
        communications = new ArrayList<>(bufferedCommunications);
        bufferedCommunications.clear();


        //Clear sounds of old update cycle and add new sounds of current cycle
        sounds = new ArrayList<>(bufferedSounds);
        bufferedSounds.clear();

            //Agent sounds, at a linear function with a slope of 4.0
        for (Guard guard : guards)
            if(guard.getActive())
                sounds.add(new Sound(new Vector2(guard.getPosition()), guard.getVelocity() * 4.0f)); //replace magic number for sound curve
        for (Intruder intruder : intruders)
            if(intruder.getActive())
                sounds.add(new Sound(new Vector2(intruder.getPosition()), intruder.getVelocity() * 4.0f)); //replace magic number for sound curve

            //Random sounds, rate 0.1 / minute / 25m^2
        for(int i = 0; i < (int)(map.getWidth() * map.getHeight() / 25.0f); i++)
            if (Math.random() < 1.0f / (600.0f * GameLoop.TICK_RATE)) {
                Vector2 randomPosition = new Vector2();
                randomPosition.x = (float) Math.random() * map.getWidth();
                randomPosition.y = (float) Math.random() * map.getHeight();
                sounds.add(new Sound(randomPosition, RANDOM_SOUND_VISIBILITY));
            }

        //Update the pheromones and remove the weak ones
        for(Pheromone pheromone : pheromones)
            pheromone.update();

        for (int i = 0; i < pheromones.size(); i++)
            if (pheromones.get(i).getIntensity() <= 0.0f)
            {
                pheromones.remove(i);
                i--;
            }

        //Then update all AIs
        for (Guard guard : guards)
            guard.update();
        for (Intruder intruder : intruders)
            intruder.update();

        //Then update all positions
        for (Guard guard : guards)
            guard.updatePosition();
        for (Intruder intruder : intruders)
            intruder.updatePosition();
    }

    public int getSimulationStartTick()
    {
        return simulationStartTick;
    }

    public Map getMap()
    {
        return map;
    }

    public Settings getSettings(){
        return settings;
    }

    public GameLoop getGameLoop()
    {
        return gameLoop;
    }

    public ArrayList<Guard> getGuards()
    {
        return guards;
    }

    public ArrayList<Intruder> getIntruders()
    {
        return intruders;
    }

    public ArrayList<Agent> getAgents()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.addAll(guards);
        agents.addAll(intruders);

        return agents;
    }

    public void addCommunication(Communication communication)
    {
        bufferedCommunications.add(communication);
    }

    //AStar Graphics
    public ArrayList<Vector2> getAStarGraphNodes(){ return aStarGraphNodes; }

    public void addAStarGraphNode(Vector2 node) { aStarGraphNodes.add(node); }

    public ArrayList<AStarAI.Pair<Vector2, Vector2>> getAStarGraphEdges(){ return aStarGraphEdges; }

    public void addAStarGraphEdge(AStarAI.Pair<Vector2, Vector2> edge) { aStarGraphEdges.add(edge); }



    public ArrayList<Communication> getCommunications() {
        return communications;
    }

    public void addSound(Sound sound)
    {
        bufferedSounds.add(sound);
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void addPheromone(Pheromone pheromone)
    {
        pheromones.add(pheromone);
    }

    public ArrayList<Pheromone> getPheromones() {
        return pheromones;
    }

}
