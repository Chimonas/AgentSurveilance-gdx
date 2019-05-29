package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.AI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.Arrays;

public class PheromoneAI extends AI
{
    public PheromoneAI(Agent agent)
    {
        super(agent);

    }

    @Override
    public void update()
    {
        super.update();

    }

    @Override
    public void spawn(Map map) {
        Vector2 randomPosition = new Vector2();
        randomPosition.x = (float) Math.random() * map.getWidth();
        randomPosition.y = (float) Math.random() * map.getHeight();

        agent.spawnPosition(randomPosition, (float)Math.random() * 360.0f);
    }

    public enum PheromoneAction
    {
        FOLLOWINTRUDER, HEARDSOUND, FOUNDSENTRYTOWER, ENTERINGBUILDING,
        ENTERINGSENTRYTOWER, FOUNDSHADE, BUILDINGCLEAR, GUARDINGBUILDING;
    }

    public void setPheromoneToAction(Pheromone.PheromoneType pherColor, PheromoneAction action)
    {
        switch(action){
            case FOLLOWINTRUDER: setFollowingIntruders(pherColor); break;
            case FOUNDSHADE: setFindingShade(pherColor); break;
            case HEARDSOUND:setHearingSound(pherColor); break;
            case BUILDINGCLEAR: setBuildingClear(pherColor); break;
            case ENTERINGBUILDING: setEnteringBuilding(pherColor); break;
            case FOUNDSENTRYTOWER: setFindingSentryTower(pherColor); break;
            case GUARDINGBUILDING: setGuardingBuilding(pherColor); break;
            case ENTERINGSENTRYTOWER: setEnteringSentryTower(pherColor); break;
        }

    }

    private void setFollowingIntruders(Pheromone.PheromoneType color){

//        System.out.println(visibleIntruders);
        if(!visibleIntruders.isEmpty())
        {
            agent.createPheromone(color);
            newVelocity = agent.MAX_VELOCITY;
        }
        else if(!visiblePheromones.isEmpty())
        {
            newVelocity = agent.MAX_VELOCITY;
        }
        else
        {
            if(agent.getVelocity() == agent.MAX_VELOCITY)
                newVelocity = 1.0f;
            else
                newVelocity = agent.getVelocity() + 0.05f / (float)GameLoop.TICK_RATE;

            newAngle = agent.getAngleFacing() + 0.2f * agent.MAX_TURN_VELOCITY / (float)GameLoop.TICK_RATE;
        }
    }

    private void setFindingShade(Pheromone.PheromoneType color){
        if(agent.inShade())
            agent.createPheromone(color);
    }

    private void setHearingSound(Pheromone.PheromoneType color){
        for(Float s: agent.getVisibleSounds())
            agent.createPheromone(color);
    }

    private void setFindingSentryTower(Pheromone.PheromoneType color){

    }

    //FOR NOW IT GENERATES PHEROMONE IF IS IN BUILDING
    private void setEnteringBuilding(Pheromone.PheromoneType color){
        for(Area a: agent.getWorld().getMap().getAreaList())
            if(a.contains(agent.getPosition()))
                agent.createPheromone(color);
    }

    private void setEnteringSentryTower(Pheromone.PheromoneType color){

    }

    private void setBuildingClear(Pheromone.PheromoneType color){

    }

    private void setGuardingBuilding(Pheromone.PheromoneType color){

    }
}
