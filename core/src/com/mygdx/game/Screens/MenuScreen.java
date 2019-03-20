package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.SurveilanceSystem;

import java.awt.*;

public class MenuScreen implements Screen {
	SpriteBatch batch;
	Stage stage;

	final Game surveilance;
	OrthographicCamera cam;
	TextButton createGameBtn, loadGameBtn;

	public MenuScreen(SurveilanceSystem surveilance) {
	    this.surveilance = surveilance;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));
        createGameBtn = new TextButton("Create Game", skin);
        loadGameBtn = new TextButton("Load Game", skin);

        createGameBtn.setPosition(Gdx.graphics.getWidth()/2 - createGameBtn.getWidth()/2,Gdx.graphics.getHeight()/2+50);
        loadGameBtn.setPosition(Gdx.graphics.getWidth()/2 - createGameBtn.getWidth()/2+20,Gdx.graphics.getHeight()/2);


        class createGameListener extends ChangeListener {

            SurveilanceSystem surveilance;
            Screen screen;

            public createGameListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Pressed");
                this.surveilance.setScreen(new BuilderScreen(surveilance));
                this.screen.dispose();
            }
        }


        class loadGameListener extends ChangeListener {

            SurveilanceSystem surveilance;
            Screen screen;

            public loadGameListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Pressed");
                this.surveilance.setScreen(new BuilderScreen(surveilance));
                this.screen.dispose();
            }
        }

        loadGameBtn.addListener(new loadGameListener(surveilance,this));
        createGameBtn.addListener(new createGameListener(surveilance,this));
        //add Actors
        stage.addActor(createGameBtn);
        stage.addActor(loadGameBtn);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 0);
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

//        System.out.println("Has been disposed");
	}
}
