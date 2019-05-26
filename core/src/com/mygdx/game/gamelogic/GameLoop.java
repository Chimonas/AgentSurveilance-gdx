package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;

public class GameLoop
{
    private World world;
    public static final double TICKRATE = 30.0f, SPEEDSTEP = 2.0f;

    private boolean running, pause, exploration;
    private int ticks;
    private double time, speed, lastTickTime, timeBeforeTick, simulationTime, explorationTime; // !All time related variables have to be in double for precision!

    public GameLoop(World world, double simulationTime, boolean exploration, double explorationTime)
    {
        this.world = world;
        this.simulationTime = simulationTime;
        this.exploration = exploration;
        this.explorationTime = explorationTime;

        pause = false;
        ticks = 0;
        time = 0.0;
        setSpeed(1.0);
    }

    public void update()
    {
        ticks++;
        lastTickTime += timeBeforeTick;
        world.update();
    }

    public void check()
    {
        if(running && !pause)
        {
            time = System.nanoTime();

            if(exploration && ticks >= (int)(explorationTime * TICKRATE))
            {
                exploration = false;
                world.startSimulationPhase();
            }

            if(simulationTime != 0.0)
                if (ticks >= (int) (explorationTime + simulationTime) * TICKRATE)
                    stop();

            checkWinningConditions();

            while (time - lastTickTime > timeBeforeTick)
                update();
        }
    }

    public void checkWinningConditions(){

        //Winning condition for Guards
        for(Agent a : this.world.getGuards())
            for (Intruder i : a.getVisibleIntruders())
                if(a.getPosition().dst(i.getPosition()) <= 0.5){
                    //TODO: Message that the guards won
                    stop();
                }

        //TODO: Winning condition for Intruders



    }

    public void start()
    {
        running = true;
        lastTickTime = System.nanoTime();

        if(exploration)
            world.startExplorationPhase();
        else
            world.startSimulationPhase();
    }

    public void stop()
    {
        running = false;
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;

        if(!pause)
            lastTickTime = System.nanoTime();
    }

    public void togglePause()
    {
        setPause(!pause);
    }

    public int getTicks()
    {
        return ticks;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
        timeBeforeTick = 1.0e9f / (TICKRATE * speed);
    }

    public void incrementSpeed()
    {
        setSpeed(speed * SPEEDSTEP);
    }

    public void decrementSpeed()
    {
        setSpeed(speed / SPEEDSTEP);
    }

}
