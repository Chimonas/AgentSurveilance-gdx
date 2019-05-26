package com.mygdx.game.worldAttributes.agents.guard.ai;

import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.worldAttributes.Pheromone;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;
import java.util.Arrays;

public class PheromoneAI extends GuardAI
{
    public PheromoneAI(Guard guard)
    {
        super(guard);

    }

    @Override
    public void update()
    {
        super.update();

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

        if(!visibleIntruders.isEmpty())
        {
            agent.createPheromone(color);
            newVelocity = agent.MAXVELOCITY;
        }
        else if(!visiblePheromones.isEmpty())
        {
            newVelocity = agent.MAXVELOCITY;
        }
        else
        {
            if(agent.getVelocity() == agent.MAXVELOCITY)
                newVelocity = 1.0f;
            else
                newVelocity = agent.getVelocity() + 0.05f / (float)GameLoop.TICKRATE;

            newAngle = agent.getAngleFacing() + 0.2f * agent.MAXTURNVELOCITY / (float)GameLoop.TICKRATE;
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
