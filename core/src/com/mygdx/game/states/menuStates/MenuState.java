package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.StateManager;
import com.mygdx.game.states.State;

public abstract class MenuState extends State
{
    public MenuState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void create()
    {
        createStage();
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Stage stage = getStage();

        if(stage != null)
        {
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
            Gdx.input.setInputProcessor(stage);
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose()
    {
        getStage().dispose();
    }

    protected abstract Stage getStage();
    protected abstract void createStage();
}
