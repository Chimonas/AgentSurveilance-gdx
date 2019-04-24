package com.mygdx.game.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.StateManager;

public abstract class State extends InputAdapter implements ApplicationListener
{
    protected StateManager stateManager;

    protected State(StateManager stateManager)
    {
        this.stateManager = stateManager;
    }

    @Override
    public abstract void create();
    public abstract void render();
    public abstract void resize(int width, int height);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();
}
