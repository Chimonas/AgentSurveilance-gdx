package com.mygdx.game.gamelogic;

public class GameLoop
{
    private World world;
    public static final double TICKRATE = 30.0f, SPEEDSTEP = 2.0f;

    private boolean pause, exploration;
    private int ticks;
    private double time, speed, lastTickTime, timeBeforeTick; // !All time related variables have to be in double for precision!

    public GameLoop(World world, boolean exploration)
    {
        this.world = world;

        setPause(false);
        ticks = 0;
        time = 0.0f;
        setSpeed(1.0f);
    }

    public void update()
    {
        ticks++;
        lastTickTime += timeBeforeTick;
        world.update();
    }

    public void check()
    {
        time = System.nanoTime();

        if(!pause)
            while(time - lastTickTime > timeBeforeTick )
                update();
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
