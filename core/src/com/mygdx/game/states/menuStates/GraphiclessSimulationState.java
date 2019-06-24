package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;

public class GraphiclessSimulationState extends MenuState
{
    private Stage stage;
    private Settings settings;
    private Map map;
    private World world;

    private int currentSimulation, guardWins, intruderWins, timeRanOut;

    public GraphiclessSimulationState(StateManager stateManager, Settings settings, Map map)
    {
        super(stateManager);
        this.settings = settings;
        this.map = map;

        currentSimulation = guardWins = intruderWins = timeRanOut = 0;
        world = new World(map, settings);

        System.out.println("[Starting Simulations]");
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
        if(world.getGameLoop().getResult() == -1)
            intruderWins++;
        else if(world.getGameLoop().getResult() == 0)
            timeRanOut++;
        else if(world.getGameLoop().getResult() == 1)
            guardWins++;

        currentSimulation++;
        world = new World(map, settings);
    }

    public void printResults()
    {
        System.out.println("Intruder wins: " + intruderWins);
        System.out.println("Time ran out: " + timeRanOut);
        System.out.println("Guard wins: " + guardWins);
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
