package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.*;

import java.util.ArrayList;

public class GraphiclessSimulationState extends MenuState
{
    private Stage stage;
    private Settings settings;
    private Map map;
    private World world;

    private int currentSimulation, guardWins, intruderWins, timeRanOut;

    private String timeToWinGuards;
    private String timeToWinIntruders;

    private float amountOfTimeToWinGuards;

    public GraphiclessSimulationState(StateManager stateManager, Settings settings, Map map)
    {
        super(stateManager);
        this.settings = settings;
        this.map = map;

        currentSimulation = guardWins = intruderWins = timeRanOut = 0;
        world = new World(map, settings);

        System.out.println("[Starting Simulations]");

        timeToWinGuards = new String();
        timeToWinIntruders = new String();

        amountOfTimeToWinGuards = 0;

    }

    @Override
    public void render()
    {
        if(currentSimulation < settings.getSimulationAmount())
        {
            if(world.getGameLoop().isRunning())
                world.getGameLoop().check();
            else
                incrementSimulation();
        }
        else
        {
            printResults();
            stateManager.pop();
        }

        super.render();
    }

    public void incrementSimulation()
    {

        System.out.println(currentSimulation + ": " + world.getGameLoop().getResult() + " " + (world.getGameLoop().getTicks() - world.getSimulationStartTick()) / GameLoop.TICK_RATE);
        if(world.getGameLoop().getResult() == -1) {
            intruderWins++;
            timeToWinIntruders += (world.getGameLoop().getTicks() - world.getSimulationStartTick()) / GameLoop.TICK_RATE + "\n";
            amountOfTimeToWinGuards += (world.getGameLoop().getTicks() - world.getSimulationStartTick()) / GameLoop.TICK_RATE;
        }
        else if(world.getGameLoop().getResult() == 0)
            timeRanOut++;
        else if(world.getGameLoop().getResult() == 1) {
            guardWins++;
            timeToWinGuards += (world.getGameLoop().getTicks() - world.getSimulationStartTick()) / GameLoop.TICK_RATE + "\n";
        }

        currentSimulation++;
        world = new World(map, settings);
    }

    public void printResults()
    {
        System.out.println("Intruder wins: " + intruderWins);
        System.out.println("Time ran out: " + timeRanOut);
        System.out.println("Guard wins: " + guardWins);


        System.out.println("Win Rate " + (float)(guardWins/(guardWins + intruderWins+timeRanOut)));
        System.out.println("Average Time " + amountOfTimeToWinGuards/guardWins);

        FileHandler.saveGuardsResults(timeToWinGuards);
        FileHandler.saveIntrudersResults(timeToWinIntruders);
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    @Override
    protected Stage getStage()
    {
        return stage;
    }

    private Label titleL;

    @Override
    public void createStage()
    {
        stage = new Stage(new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table table = new Table();
        table.setFillParent(true);

        titleL = new Label("WARNING: SIMULATION IN PRROGRESS!!", StateManager.skin, "title");
        table.add(titleL).center().top().height(100f);
        table.row();

        table.pad(50f);

        stage.addActor(table);
    }
}
