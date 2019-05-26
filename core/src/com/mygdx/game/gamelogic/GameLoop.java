package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Target;

public class GameLoop
{
    private World world;
    public static final double TICKRATE = 30.0f, SPEEDSTEP = 2.0f;

    private boolean running, pause, exploration;
    private int ticks;
    private double time, speed, lastTickTime, timeBeforeTick, simulationTime, explorationTime, targetAreaTimer, enteredAreaTime; // !All time related variables have to be in double for precision!

    public GameLoop(World world, double simulationTime, boolean exploration, double explorationTime)
    {
        this.world = world;
        this.simulationTime = simulationTime;
        this.exploration = exploration;
        this.explorationTime = explorationTime;

        pause = false;
        ticks = 0;
        time = 0.0;
        targetAreaTimer = 3.0;
        enteredAreaTime = -1;
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
        for(Guard g : this.world.getGuards())
            for (Intruder i : g.getVisibleIntruders())
                if(g.getPosition().dst(i.getPosition()) <= 0.5)
                    stop(); //TODO: Message that the guards won

        //Winning condition for Intruders
        for(Intruder i : this.world.getIntruders())
            for(Area a : this.world.getMap().getAreaList())
                if(a instanceof Target && a.contains(i.getPosition())) {
                    if(i.enteredAreaTime == -1) i.enteredAreaTime = System.nanoTime();
                    if(Math.pow(10,-9) * (System.nanoTime() - i.enteredAreaTime) * TICKRATE >= (targetAreaTimer * TICKRATE))
                        stop();
                } else i.enteredAreaTime = -1; //TODO: Message that intruders won


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
