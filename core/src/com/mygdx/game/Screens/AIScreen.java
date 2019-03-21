package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.SurveilanceSystem;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

public class AIScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    private final SurveilanceSystem surveilance;
    private final String TITLE = "AI Simulation Settings";
    OrthographicCamera cam;

    Label agents, guard, intruder,  time, explPhase, weights;
    float width = 800;
    float height = 40;
    TextField nmbGuard, nmbIntr, explTime, maxTime;
    SelectBox<String> guardSB, intrSB;
    CheckBox explpCB;

    public AIScreen(SurveilanceSystem surveilance){
        this.surveilance = surveilance;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));

        //Agents
        agents = new Label("Agents",skin);
        agents.setPosition(10,Gdx.graphics.getHeight()-40);
        agents.setSize(100,height);

        guard = new Label("Guard AI:", skin);
        guard.setPosition(200, Gdx.graphics.getHeight()-50);
        guard.setSize(100,height);
        guardSB = new SelectBox<String>(skin);
        guardSB.setItems("AI 1", "AI 2");
        guardSB.setSize(100,height);
        guardSB.setPosition(400,Gdx.graphics.getHeight()-50);
        nmbGuard = new TextField("Number of guards:",skin);
        nmbGuard.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-100);
        nmbGuard.setSize(width,height);

        intruder = new Label("Guard AI:", skin);
        intruder.setPosition(200, Gdx.graphics.getHeight()-150);
        intruder.setSize(100,height);
        intrSB = new SelectBox<String>(skin);
        intrSB.setItems("AI 1", "AI 2");
        intrSB.setSize(100,height);
        intrSB.setPosition(400,Gdx.graphics.getHeight()-150);
        nmbIntr = new TextField("Number of intruders:",skin);
        nmbIntr.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-200);
        nmbIntr.setSize(width,height);

        //Time
        time = new Label("Time",skin);
        time.setPosition(10,Gdx.graphics.getHeight()-290);
        time.setSize(100,height);

        maxTime = new TextField("Maximum time (s)", skin);
        maxTime.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-300);
        maxTime.setSize(width,height);
        explpCB = new CheckBox("Exploration phase", skin);
        explpCB.setPosition(100,Gdx.graphics.getHeight()-350);
        explpCB.setSize(width,height);
        //explPhase = new Label("Exploration phase", skin);
        //explPhase.setPosition(300,Gdx.graphics.getHeight()-350);
        //explPhase.setSize(100,height);
        explTime = new TextField("Exploration time (s)", skin);
        explTime.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-400);
        explTime.setSize(width,height);

        //Agents actors
        stage.addActor(agents);
        stage.addActor(guard);
        stage.addActor(guardSB);
        stage.addActor(nmbGuard);
        stage.addActor(intruder);
        stage.addActor(intrSB);
        stage.addActor(nmbIntr);

        //Time actors
        stage.addActor(time);
        stage.addActor(maxTime);
        stage.addActor(explpCB);
        //stage.addActor(explPhase);
        stage.addActor(explTime);
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
        batch.dispose();
        stage.dispose();
    }
}
