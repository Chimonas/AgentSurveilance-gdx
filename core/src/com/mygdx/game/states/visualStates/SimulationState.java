package com.mygdx.game.states.visualStates;

import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.states.visualStates.drawers.WorldDrawer;

public class SimulationState extends VisualState
{
    private World world;
    private GameLoop gameLoop;

    public SimulationState(StateManager stateManager, Settings settings, Map map)
    {
        super(stateManager, map);
        world = new World(map, settings);
        gameLoop = new GameLoop(world);
    }

    @Override
    public void create()
    {
        super.create();
    }

    @Override
    public void render()
    {
        gameLoop.check();
        super.render();

        WorldDrawer.render(shapeRenderer,world);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
