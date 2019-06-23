package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Target;

public class GameLoop
{
    private World world;
    public static final double TICK_RATE = 30.0f, SPEED_STEP = 2.0f;

    private final float GUARD_WINNING_DISTANCE = 0.5f, INTRUDER_WINNING_TIME = 3.0f;

    private boolean running, pause, multipleSimulation, exploration, exploring;
    private int ticks, firstIntruderInTargetTick, result;
    private double time, speed, lastTickTime, timeBeforeTick, simulationTime, explorationTime; // !All time related variables have to be in double for precision!

    public GameLoop(World world, boolean multipleSimulations, double simulationTime, boolean exploration, double explorationTime)
    {
        this.world = world;
        this.multipleSimulation = multipleSimulations;
        this.simulationTime = simulationTime;
        this.exploration = exploration;
        this.explorationTime = explorationTime;
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

            while (running && time - lastTickTime > timeBeforeTick)
            {
                update();
                if(!exploring)
                    checkWinningConditions();
            }

            if(exploring && ticks >= (int)(explorationTime * TICK_RATE))
            {
                exploring = false;
                world.startSimulationPhase();
            }
        }
    }

    public void checkWinningConditions()
    {
        //Time Runs out
        if(simulationTime > 0.0)
            if (ticks >= (int)(explorationTime + simulationTime * TICK_RATE))
            {
                result = 0;
                stop();
            }

        //Winning condition for Guards
        for(Guard guard : world.getGuards())
            for (Intruder i : guard.getVisibleIntruders())
                if(guard.getPosition().dst2(i.getPosition()) <= GUARD_WINNING_DISTANCE * GUARD_WINNING_DISTANCE) //Using dst2 so no squareroot has to be calculated.
                {
                    result = 1;
//                    System.out.println((ticks - world.getSimulationStartTick()) / TICK_RATE);
                    stop(); //TODO: Message that the guards won
                }

        //Winning condition for Intruders
        if(checkIntrudersInTarget())
        {
            if(ticks < firstIntruderInTargetTick)
                firstIntruderInTargetTick = ticks;

            if(ticks - firstIntruderInTargetTick >= INTRUDER_WINNING_TIME * TICK_RATE)
            {
                result = -1;
                stop(); //TODO: Message that the intruders won
            }
        }
    }

    public boolean checkIntrudersInTarget()
    {
        for(Target target : world.getMap().getTargets())
            for(Intruder intruder : world.getIntruders())
                if(target.contains(intruder.getPosition()))
                    return true;

        return false;
    }

    public void start()
    {
        running = true;
        ticks = 0;
        firstIntruderInTargetTick = Integer.MAX_VALUE;

        pause = false;
        exploring = exploration;
        time = 0.0;
        lastTickTime = System.nanoTime();
        setSpeed(1.0);

        if(exploration)
            world.startExplorationPhase();
        else
            world.startSimulationPhase();
    }

    public void stop()
    {
        running = false;
    }

    public boolean isRunning()
    {
        return running;
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

    public int getResult()
    {
        return result;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
        if(!multipleSimulation)
            timeBeforeTick = 1.0e9f / (TICK_RATE * speed);
        else
            timeBeforeTick = 0.0f;
    }

    public void incrementSpeed()
    {
        setSpeed(speed * SPEED_STEP);
    }

    public void decrementSpeed()
    {
        setSpeed(speed / SPEED_STEP);
    }

}
