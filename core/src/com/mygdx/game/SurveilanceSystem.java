package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Screens.MenuScreen;

public class SurveilanceSystem extends Game {

    public static final float VIRTUAL_HEIGHT = 800;
    public static final float VIRTUAL_WIDTH = 1000;

    @Override
    public void create() {
        this.setScreen(new MenuScreen(this));
    }
}
