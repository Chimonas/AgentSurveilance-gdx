package com.mygdx.game.gamelogic;

public class GameLoop
{
    private World world;
    public static final float TICKRATE = 60.0f;

    private int ticks;
    private double time, lastTickTime, timeBeforeTick; // !All time related variables have to be in double for precision!

    public GameLoop(World world)
    {
        this.world = world;

        ticks = 0;
        time = 0;
        lastTickTime = System.nanoTime();

        timeBeforeTick = 1.0e9f / TICKRATE;
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

        while(time - lastTickTime > timeBeforeTick)
            update();
    }
}
