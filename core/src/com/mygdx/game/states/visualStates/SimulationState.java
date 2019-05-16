package com.mygdx.game.states.visualStates;

import com.badlogic.gdx.Input;
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
    }

    @Override
    public void create()
    {
        super.create();
    }

    @Override
    public void render()
    {
        world.getGameLoop().check();
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

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.COMMA)
            world.getGameLoop().decrementSpeed();
        else if(keycode == Input.Keys.PERIOD)
            world.getGameLoop().incrementSpeed();
        else if(keycode == Input.Keys.P)
            world.getGameLoop().togglePause();

        return super.keyDown(keycode);
    }
}
