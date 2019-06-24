package com.mygdx.game.gamelogic;

import com.mygdx.game.StateManager;
import com.mygdx.game.worldAttributes.agents.AStarAI;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.agents.intruder.Intruder;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Target;

import java.util.ArrayList;

public class GameLoop
{
    private final long startSimulationTime;
    private World world;

    public static final double TICK_RATE = 30.0f, SPEED_STEP = 2.0f;

    private final float GUARD_WINNING_DISTANCE = 0.5f, INTRUDER_WINNING_TIME = 3.0f;

    private boolean running, pause, exploration, exploring;
    private int ticks, firstIntruderInTargetTick;
    private double time, speed, lastTickTime, timeBeforeTick, simulationTime, explorationTime; // !All time related variables have to be in double for precision!

    public ArrayList<AStarAI.Pair<Guard,Double>> guardWinners;
    public ArrayList<AStarAI.Pair<Intruder, Double>> intruderWinners;

    public StateManager sm;

    Boolean geneticAlgo;

    public GameLoop(World world, double simulationTime, boolean exploration, double explorationTime)
    {
        this.world = world;
        this.simulationTime = simulationTime;
        this.exploration = exploration;
        this.explorationTime = explorationTime;
        this.sm = sm;
        this.startSimulationTime = System.currentTimeMillis();
        this.intruderWinners = new ArrayList<AStarAI.Pair<Intruder, Double>>();
        this.guardWinners = new ArrayList<AStarAI.Pair<Guard, Double>>();
        this.geneticAlgo = false;
//        pause = false;
//        ticks = 0;
//        time = 0.0;
//        targetAreaTimer = 3.0;
//        enteredAreaTime = -1;
//        setSpeed(1.0);
    }

    public void update()
    {
        ticks++;
        lastTickTime += timeBeforeTick;
        world.update();
    }

    float startExplorationTIme = System.nanoTime();

    public void check() {
        if (running && !pause) {
            time = System.nanoTime();

            if (exploring && ticks >= (int) (explorationTime * TICK_RATE)) {
                exploring = false;
                world.startSimulationPhase();
            }

            if (simulationTime != 0.0)
                if (ticks >= (int) (explorationTime + simulationTime) * TICK_RATE) {
                    System.out.println("Simulation time done");

                    stop();
                }

            while (running && time - lastTickTime > timeBeforeTick) {
                update();
                checkWinningConditions();
            }
        }
    }

    public void checkWinningConditions ()
    {
        //Winning condition for Guards
        for (Guard guard : world.getGuards())
            for (Intruder i : guard.getVisibleIntruders())
                if (guard.getPosition().dst2(i.getPosition()) <= GUARD_WINNING_DISTANCE * GUARD_WINNING_DISTANCE) //Using dst2 so no squareroot has to be calculated.
                {
                    guardWinners.add(new AStarAI.Pair<Guard, Double>(guard, (ticks-world.getSimulationStartTick())/TICK_RATE));
                    System.out.println("Guards won" + System.nanoTime());
                    System.out.println((ticks - world.getSimulationStartTick()) / TICK_RATE);
                    stop();
                }

        //Winning condition for Intruders
        if (checkIntruderInTarget()) {
            if (ticks < firstIntruderInTargetTick)
                firstIntruderInTargetTick = ticks;

            if (ticks - firstIntruderInTargetTick >= INTRUDER_WINNING_TIME * TICK_RATE) {
                intruderWinners.add(new AStarAI.Pair<Intruder, Double>(getInruderInTarget(), (ticks-world.getSimulationStartTick())/TICK_RATE));
                System.out.println("Intruders won");
                System.out.println((ticks - world.getSimulationStartTick()) / TICK_RATE);
                stop();
            }
        }
    }

    public boolean checkIntruderInTarget ()
    {
        for (Area area : world.getMap().getAreaList())
            if (area instanceof Target)
                for (Intruder intruder : world.getIntruders())
                    if (area.contains(intruder.getPosition()))
                        return true;

        return false;
    }

    public Intruder getInruderInTarget(){
        for (Area area : world.getMap().getAreaList())
            if (area instanceof Target)
                for (Intruder intruder : world.getIntruders())
                    if (area.contains(intruder.getPosition()))
                        return intruder;

        return null;
    }

    public void start ()
    {
        running = true;
        ticks = 0;
        firstIntruderInTargetTick = Integer.MAX_VALUE;

        pause = false;
        exploring = exploration;
        time = 0.0;
        lastTickTime = System.nanoTime();
        setSpeed(1.0);

        if (exploration)
            world.startExplorationPhase();
        else
            world.startSimulationPhase();
    }

    public void stop ()
    {
        running = false;

    }

    public void setPause ( boolean pause)
    {
        this.pause = pause;

        if (!pause)
            lastTickTime = System.nanoTime();
    }

    public void togglePause ()
    {
        setPause(!pause);
    }

    public int getTicks ()
    {
        return ticks;
    }

    public boolean getExploring (){return exploring;}

    public void setSpeed ( double speed)
    {
        this.speed = speed;
        timeBeforeTick = 1.0e9f / (TICK_RATE * speed);
    }

    public void incrementSpeed ()
    {
        setSpeed(speed * SPEED_STEP);
    }

    public void decrementSpeed ()
    {
        setSpeed(speed / SPEED_STEP);
    }

}
