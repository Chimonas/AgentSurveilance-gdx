package com.mygdx.game.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.Communication;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

import java.util.ArrayList;

public class World
{
    private static final float RANDOMSOUNDVISIBILITY = 5.0f;

    Map map;
    GameLoop gameLoop;
    Settings settings;
    ArrayList<Guard> guards;
    ArrayList<Intruder> intruders;
    ArrayList<Communication> communications;
    ArrayList<Sound> sounds;
    ArrayList<Pheromone> pheromones;

    public World(Map map, Settings settings)
    {
        this.map = map;
        this.settings = settings;
        gameLoop = new GameLoop(this, settings.getMaxTime(), settings.isExplorationPhase(), settings.getExplorationTime());

        guards = new ArrayList<>();
        intruders = new ArrayList<>();
        communications = new ArrayList<>();
        sounds = new ArrayList<>();
        pheromones = new ArrayList<>();

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
            guard.spawn();
        }

        // V THIS V = BIG NO NO
//        int increment = 0;
//        for(Guard guard : guards)
//        {
//            guard.setExplorationAI(settings.getExplorationAIType());
//
//            if(settings.getExplorationAIType() == ExplorationAI.ExplorationAIType.HEURISTIC) {
//                divideMap = map.getWidth() / guards.size();
//                guard.spawnLeft(map, divideMap, increment);
//            }
//            else
//                guard.spawnRandom(map);
//            increment++;
//        }
    }

    public void startSimulationPhase()
    {
        for(Guard guard : guards)
        {
            guard.setSimulationAI(settings.getGuardAIType());
            guard.spawn();
        }

        for(Intruder intruder : intruders)
        {
            intruder.setIntruderAI(settings.getIntruderAIType());
            intruder.spawn();
        }

        pheromones.clear();
    }

    public void update()
    {
        communications.clear();
        sounds.clear();

        for(int i = 0; i < (int)(map.getWidth() * map.getHeight() / 25.0f); i++)
            if (Math.random() < 1.0f / (600.0f * GameLoop.TICK_RATE)) {
                Vector2 randomPosition = new Vector2();
                randomPosition.x = (float) Math.random() * map.getWidth();
                randomPosition.y = (float) Math.random() * map.getHeight();
                addSound(new Sound(randomPosition, RANDOMSOUNDVISIBILITY));
            }

        for(Pheromone pheromone : pheromones)
            pheromone.update();

        for (int i = 0; i < pheromones.size(); i++)
            if (pheromones.get(i).getIntensity() <= 0.0f)
            {
                pheromones.remove(i);
                i--;
            }

        for (Guard guard : guards)
            guard.update();

        for (Intruder intruder : intruders)
            intruder.update();


        for (Guard guard : guards)
            guard.updatePosition();

        for (Intruder intruder : intruders)
            intruder.updatePosition();
    }

    public Map getMap()
    {
        return map;
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

    public ArrayList<Communication> getCommunications() {
        return communications;
    }

    public void addCommunication(Communication communication)
    {
        communications.add(communication);
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void addSound(Sound sound)
    {
        sounds.add(sound);
    }

    public ArrayList<Pheromone> getPheromones() {
        return pheromones;
    }

    public void addPheromone(Pheromone pheromone)
    {
        pheromones.add(pheromone);
    }
}
