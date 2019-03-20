package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.SurveilanceSystem;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AIScreen implements Screen {
    SpriteBatch batch;
    Stage stage;

    final SurveilanceSystem surveilance;
    private final String TITLE = "AI Simulation Settings";
    OrthographicCamera cam;

    Label agents, time, weights;
    float width = 800;
    float height = 40;
    TextField nmbGuard, nmbIntr;

    public AIScreen(SurveilanceSystem surveilance){
        this.surveilance = surveilance;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));

        agents = new Label("Agents",skin);
        agents.setPosition(0,Gdx.graphics.getHeight());
        agents.setSize(100,height);

        nmbGuard = new TextField("Number of guards:",skin);
        nmbGuard.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-100);
        nmbGuard.setSize(width,height);

        nmbIntr = new TextField("Number of intruders:",skin);
        nmbIntr.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-200);
        nmbIntr.setSize(width,height);

        stage.addActor(agents);
        stage.addActor(nmbGuard);
        stage.addActor(nmbIntr);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
//		batch.draw(img, 0, 0);
        batch.end();

        stage.act();
        stage.draw();
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
    public void hide() {

    }

    @Override
    public void dispose () {

    }
}
