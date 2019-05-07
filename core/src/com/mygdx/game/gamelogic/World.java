package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

import java.util.ArrayList;

public class World
{
    Map map;
    Settings settings;
    ArrayList<Guard> guards;
    ArrayList<Intruder> intruders;
    ArrayList<Sound> sounds;
    ArrayList<Pheromone> pheromones;

    public World(Map map, Settings settings)
    {
        this.map = map;
        this.settings = settings;

        guards = new ArrayList<>();
        intruders = new ArrayList<>();
        sounds = new ArrayList<>();
        pheromones = new ArrayList<>();

        for (int i = 0; i < 10; i++) //10 magic number for settings.getGuardAmount
        {
            Guard newGuard = new Guard(this, settings);
            newGuard.spawnRandom(map);
            guards.add(newGuard);
        }
        for (int i = 0; i < 10; i++) //10 magic number for settings.getIntruderAmount
        {
            Intruder newIntruder = new Intruder(this, settings);
            newIntruder.spawnRandom(map);
            intruders.add(newIntruder);
        }
    }

    public void update()
    {
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

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public ArrayList<Pheromone> getPheromones() {
        return pheromones;
    }
}
