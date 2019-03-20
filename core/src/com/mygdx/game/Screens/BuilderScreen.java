package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.GameLogic.BuilderTools;
import com.mygdx.game.GameLogic.Map;
import com.mygdx.game.SurveilanceSystem;


public class BuilderScreen implements Screen {
    SpriteBatch batch;
    Stage stage;

    final SurveilanceSystem surveilance;
    private BuilderTools builderTools;
    private Map map;
    OrthographicCamera cam;
    TextButton runGame;

    public BuilderScreen(SurveilanceSystem surveilance)
    {
        this.batch = new SpriteBatch();
        stage = new Stage();
        this.surveilance = surveilance;
        builderTools = new BuilderTools();
        map = new Map(200,200);

        //Camera does absolutely nothing
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, surveilance.VIRTUAL_WIDTH, surveilance.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));
        runGame = new TextButton("Run", skin);
        runGame.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-150);

//        System.out.println("Builder Screen is created");
        class runGameListener extends ChangeListener {
            SurveilanceSystem surveilance;
            Screen screen;

            public runGameListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                System.out.println("Pressed");
                this.surveilance.setScreen(new AIScreen(surveilance));
                this.screen.dispose();
            }
        }
        runGame.addListener(new runGameListener(surveilance,this));

        //add Actors
        //stage.addActor(runGame);
    }

    @Override
    public void show() {
//        System.out.println("Builder screen show");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Setup the builder tools stage
        batch.begin();
        batch.draw(new Texture("core/assets/GreyArea.png"),Gdx.graphics.getWidth()*4/5,0,Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight());
        map.render(batch);
        batch.end();

        //Add extra buttons
        stage.addActor(runGame);

        //Getting the components from the BuildTools
        stage = builderTools.getStage();

        //Managing the Input processors
        InputProcessor mouseListener = new mouseListener();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mouseListener);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //Show the BuildTools components
        stage.act();
        stage.draw();

    }


    int startX, startY;
    boolean drawing = false;

    class mouseListener implements InputProcessor {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            if (screenX < 800) {
                startX = screenX;
                startY = screenY;
                drawing = true;
                return true;
            }

            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {

            if (screenX < 800 && drawing) {
                System.out.println("Create a rectangle now");
                System.out.println("With startX coordinates: " + startX);
                System.out.println("With startY coordinates: " + startY);
                int endX = screenX;
                int endY = screenY;
                System.out.println("With endX coordinates: " + endX);
                System.out.println("With endY coordinates: " + endY);
                map.addArea(startX, Gdx.graphics.getHeight() - startY, endX, Gdx.graphics.getHeight() - endY);
                drawing = false;
            }

            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
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
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();;
        stage.dispose();

    }
}
