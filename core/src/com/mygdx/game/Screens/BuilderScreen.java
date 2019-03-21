package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.GameLogic.AreaFactory;
import com.mygdx.game.GameLogic.BuilderTools;
import com.mygdx.game.GameLogic.Map;
import com.mygdx.game.SurveilanceSystem;


public class BuilderScreen implements Screen
{
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Stage stage;

    private BuilderTools builderTools;
    private Map map;
    private Area drawingArea;
    private TextButton runGame;

    //    private final SurveilanceSystem surveilance;

    public BuilderScreen(SurveilanceSystem surveilance)
    {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage();
        builderTools = new BuilderTools();
        map = new Map();

        double aspectRatio = Gdx.graphics.getHeight()/Gdx.graphics.getWidth();

//        this.surveilance = surveilance;

        //Camera does absolutely nothing
        cam = new OrthographicCamera((float)(map.getHeight() * aspectRatio + 200), (float)map.getHeight());
        cam.position.set((float)map.getWidth(), (float)map.getHeight(), 0);

        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));
        runGame = new TextButton("Run", skin);
        runGame.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-150);

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
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Setup the builder tools stage
        cam.update();

        batch.begin();
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        map.render(batch,shapeRenderer);
        if(drawing)
            drawingArea.render(batch, shapeRenderer);
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

    private double[] startPoint, endPoint;
    boolean drawing = false;
    boolean colliding;

    class mouseListener implements InputProcessor
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            if (screenX < 800 && AreaFactory.getAreaType() != null)
            {
                startPoint = new double[]{screenX,Gdx.graphics.getHeight() - screenY};
                drawingArea = AreaFactory.newArea(startPoint);
                drawing = true;
                return true;
            }

            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            if(drawing)
            {
                endPoint = new double[]{screenX,Gdx.graphics.getHeight() - screenY};
                drawingArea.setCoordinates(startPoint,endPoint);
            }

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean keyDown(int keycode)
        {
            if(keycode == Input.Keys.RIGHT)
            {
                cam.translate(1f,0f);
            }
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
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            if (screenX < 800 && drawing)
            {
                colliding = false;

                for(Area area : map.getAreaList())
                    if(drawingArea.intersects(area))
                    {
                        colliding = true;
                        System.out.println("col");
                    }

                if(!colliding)
                    map.addArea(drawingArea);
            }
            drawing = false;

            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            System.out.println(amount);
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
    public void dispose()
    {
        batch.dispose();
        stage.dispose();
    }
}
