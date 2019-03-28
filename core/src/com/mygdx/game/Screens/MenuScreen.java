package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.SurveilanceSystem;

public class MenuScreen implements Screen
{
	Stage stage;

	final Game surveilance;
	OrthographicCamera cam;
	TextButton createGame, runGame;

	public MenuScreen(SurveilanceSystem surveilance) {
	    this.surveilance = surveilance;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));
        createGame = new TextButton("Create Game", skin);
        createGame.setPosition(Gdx.graphics.getWidth()/2 - createGame.getWidth()/2,Gdx.graphics.getHeight()/2 +50);

        runGame = new TextButton("Run", skin);
        runGame.setPosition(Gdx.graphics.getWidth()/2 - runGame.getWidth()/2,Gdx.graphics.getHeight()/2 +100);


        class createGameListener extends ChangeListener {
            SurveilanceSystem surveilance;
            Screen screen;

            public createGameListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                this.surveilance.setScreen(new BuilderScreen(surveilance));
                this.screen.dispose();
            }
        }
        createGame.addListener(new createGameListener(surveilance,this));

        class runGameListener extends ChangeListener {
            SurveilanceSystem surveilance;
            Screen screen;

            public runGameListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                this.surveilance.setScreen(new AIScreen(surveilance));
                this.screen.dispose();
            }
        }
        runGame.addListener(new runGameListener(surveilance,this));


        //add Actors
        stage.addActor(createGame);
        stage.addActor(runGame);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.8f, .8f, .8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
	public void dispose ()
    {
		stage.dispose();
	}
}
