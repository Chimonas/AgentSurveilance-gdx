package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.states.menuStates.MainMenuState;
import com.mygdx.game.states.State;

import java.util.Stack;

public class StateManager extends ApplicationAdapter
{
    public static final int WIDTH = 1280, HEIGHT = 720;
    public static Skin skin;

    private Stack<State> states;

    public StateManager(){
    }

    public void create()
    {
        skin = new Skin(Gdx.files.internal("core/assets/Skins/cloud-form/skin/cloud-form-ui.json"));
        states = new Stack<>();

        Gdx.gl.glClearColor(0,0,1,1);

        State start = new MainMenuState(this);
        start.create();
        states.push(start);
    }

    public void push(State state)
    {
        state.create();
        states.push(state);
    }

    public void pop()
    {
        states.peek().dispose();
        states.pop();
    }

    public void set(State state){
        states.pop();
        state.create();
        states.push(state);
    }

    public void home()
    {
        while(!(states.peek() instanceof MainMenuState))
            states.pop();
    }

    @Override
    public void render()
    {
        states.peek().render();
    }

    @Override
    public void dispose()
    {
        skin.dispose();
    }
}
