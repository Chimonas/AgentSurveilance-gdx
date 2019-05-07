package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.Sound;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public abstract class GuardAI implements AI
{
    public enum AIType
    {
        STUPID
    }

    protected Guard guard;
    protected ArrayList<Area> visibleAreas;
    protected ArrayList<Agent> visibleAgents;
    protected ArrayList<Sound> visibleSounds;
    protected ArrayList<Pheromone> visiblePheromones;

    public GuardAI(Guard guard)
    {
        this.guard = guard;
    }

    public void update()
    {
        visibleAreas = guard.getVisibleAreas();
        visibleAgents = guard.getVisibleAgents();
        visibleSounds = guard.getVisibleSounds();
        visiblePheromones = guard.getVisiblePheromones();
    }
}
