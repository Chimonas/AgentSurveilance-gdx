package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.AI.Guard;
import com.mygdx.game.AI.Intruder;
import com.mygdx.game.GameLogic.Map;
import com.mygdx.game.GameLogic.Settings;
import com.mygdx.game.SurveilanceSystem;

public class AIScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    private final SurveilanceSystem surveilance;
    private Map map;
    private final String TITLE = "AI Simulation Settings";
    OrthographicCamera cam;

    Label agents, guard, intruder,  time, weights, nmbGuards, nmbIntruders;
    float width = 800;
    float height = 40;
    TextField nmbGuardTF, nmbIntrTF, explTimeTF, maxTimeTF, timeTF, movementTF, dirComTF, indirComTF;
    SelectBox<String> guardSB, intrSB;
    CheckBox explpCB;
    TextButton okButton, cancelButton;

    public AIScreen(SurveilanceSystem surveilance){
        this.surveilance = surveilance;
        this.map = map;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));

        //Agents
        agents = new Label("Agents",skin);
        agents.setPosition(10,Gdx.graphics.getHeight()-50);
        agents.setSize(100,height);

        guard = new Label("Guard AI:", skin);
        guard.setPosition(200, Gdx.graphics.getHeight()-50);
        guard.setSize(100,height);
        guardSB = new SelectBox<String>(skin);
        guardSB.setItems("AI 1", "AI 2");
        guardSB.setSize(100,height);
        guardSB.setPosition(400,Gdx.graphics.getHeight()-50);

        nmbGuards = new Label("Number of guards :  ", skin);
        nmbGuards.setPosition(10,Gdx.graphics.getHeight()-100);
        nmbGuards.setSize(100,height);
        nmbGuardTF = new TextField("", skin);
        nmbGuardTF.setPosition((Gdx.graphics.getWidth()-width),Gdx.graphics.getHeight()-100);
        nmbGuardTF.setSize((float) (width/1.2),height);

        intruder = new Label("Intruder AI :", skin);
        intruder.setPosition(200, Gdx.graphics.getHeight()-150);
        intruder.setSize(100,height);
        intrSB = new SelectBox<String>(skin);
        intrSB.setItems("AI 1", "AI 2");
        intrSB.setSize(100,height);
        intrSB.setPosition(400,Gdx.graphics.getHeight()-150);
        nmbIntruders = new Label("Number of intruders :  ", skin);
        nmbIntruders.setPosition(10,Gdx.graphics.getHeight()-200);
        nmbIntruders.setSize(100,height);
        nmbIntrTF = new TextField("",skin);
        nmbIntrTF.setPosition((Gdx.graphics.getWidth()-width),Gdx.graphics.getHeight()-200);
        nmbIntrTF.setSize((float) (width/(1.2)),height);

        //Time
        time = new Label("Time",skin);
        time.setPosition(10,Gdx.graphics.getHeight()-290);
        time.setSize(100,height);

        maxTimeTF = new TextField("Maximum time (s) : ", skin);
        maxTimeTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-300);
        maxTimeTF.setSize(width,height);
        explpCB = new CheckBox(" Exploration phase", skin);
        explpCB.setPosition(100,Gdx.graphics.getHeight()-350);
        explpCB.setSize(width,height);
        explTimeTF = new TextField("Exploration time (s) : ", skin);
        explTimeTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-400);
        explTimeTF.setSize(width,height);

        //Weights
        weights = new Label("Weights : ",skin);
        weights.setPosition(10,Gdx.graphics.getHeight()-390);
        weights.setSize(100,height);

        timeTF = new TextField("Time : ", skin);
        timeTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-400);
        timeTF.setSize(width,height);
        movementTF = new TextField("Movement : ", skin);
        movementTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-450);
        movementTF.setSize(width,height);
        dirComTF = new TextField("Direct communication : ", skin);
        dirComTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-500);
        dirComTF.setSize(width,height);
        indirComTF = new TextField("Indirect communication : ", skin);
        indirComTF.setPosition((Gdx.graphics.getWidth()-width)/2,Gdx.graphics.getHeight()-550);
        indirComTF.setSize(width,height);

        //ending buttons
        okButton = new TextButton("OK", skin);
        okButton.setPosition(Gdx.graphics.getWidth()/2 + 50,Gdx.graphics.getHeight()-700);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(Gdx.graphics.getWidth()/2 - 100,Gdx.graphics.getHeight()-700);

        //Listeners agents
        selectBoxListener(guardSB);
        textFieldListener(nmbGuardTF);
        selectBoxListener(intrSB);
        textFieldListener(nmbIntrTF);

        //Listeners time
        textFieldListener(maxTimeTF);
        explpCB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.setExplorationPhase(explpCB.isChecked());
            }
        });
        textFieldListener(explTimeTF);

        //Listeners weights
        textFieldListener(timeTF);
        textFieldListener(movementTF);
        textFieldListener(dirComTF);
        textFieldListener(indirComTF);

        //Listeners ending buttons
        class okListener extends ChangeListener {
            SurveilanceSystem surveilance;
            Screen screen;

            public okListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SimulationScreen simulationScreen = new SimulationScreen(surveilance);
                simulationScreen.addMap(map);
                this.surveilance.setScreen(simulationScreen);
                this.screen.dispose();
            }
        }
        okButton.addListener(new okListener(surveilance, this));
        class cancelListener extends ChangeListener {
            SurveilanceSystem surveilance;
            Screen screen;

            public cancelListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                this.surveilance.setScreen(new MenuScreen(surveilance));
                this.screen.dispose();
            }
        }
        cancelButton.addListener(new cancelListener(surveilance, this));

        //Agents actors
        stage.addActor(agents);
        stage.addActor(guard);
        stage.addActor(nmbGuards);
        stage.addActor(guardSB);
        stage.addActor(nmbGuardTF);
        stage.addActor(intruder);
        stage.addActor(nmbIntruders);
        stage.addActor(intrSB);
        stage.addActor(nmbIntrTF);

        //Time actors
        stage.addActor(time);
        stage.addActor(maxTimeTF);
        stage.addActor(explpCB);
        stage.addActor(explTimeTF);

        //Weight actors
        stage.addActor(weights);
        stage.addActor(timeTF);
        stage.addActor(movementTF);
        stage.addActor(dirComTF);
        stage.addActor(indirComTF);

        //Ending actors
        stage.addActor(okButton);
        stage.addActor(cancelButton);
    }

    public void selectBoxListener(final SelectBox selectBox){
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(selectBox == guardSB)
                    switch (selectBox.getSelectedIndex()){
                        case 0:
                            Settings.setGuardAI(AI.GuardAI.STUPID);
                            break;
                        case 1:
                            //guardAI = ;
                            break;
                    }
                if(selectBox == intrSB)
                    switch (selectBox.getSelectedIndex()){
                        case 0:
                            Settings.setIntruderAI(AI.IntruderAI.STUPID);
                            break;
                        case 1:
                            //intruderAI = ;
                            break;
                    }
            }
        });
    }

    public void textFieldListener(final TextField textField){
        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(textField == nmbGuardTF)
                    Settings.setGuardAmount(Integer.parseInt(nmbGuardTF.getText()));
                if(textField == nmbIntrTF)
                    Settings.setIntruderAmount(Integer.parseInt(nmbIntrTF.getText()));
                if(textField == explTimeTF)
                    Settings.setExplorationTime(Integer.parseInt(explTimeTF.getText()));
                if(textField == maxTimeTF)
                    Settings.setMaxTime(Integer.parseInt(maxTimeTF.getText()));
                if(textField == timeTF)
                    Settings.setTimeWeight(Integer.parseInt(timeTF.getText()));
                if(textField == movementTF)
                    Settings.setMovementWeight(Integer.parseInt(movementTF.getText()));
                if(textField == dirComTF)
                    Settings.setDirectComWeight(Integer.parseInt(dirComTF.getText()));
                if(textField == indirComTF)
                    Settings.setIndirectComWeight(Integer.parseInt(indirComTF.getText()));
            }
        });
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

    public void addMap(Map map){
        this.map = map;
    }

}
