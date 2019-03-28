package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.Areas.AreaFactory;
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


    double aspectRatio;

    public BuilderScreen(SurveilanceSystem surveilance)
    {
        shapeRenderer = new ShapeRenderer();
        stage = new Stage();
        builderTools = new BuilderTools();
        map = new Map();

//        this.surveilance = surveilance;

        aspectRatio = (double)Gdx.graphics.getWidth()/Gdx.graphics.getHeight();

        cam = new OrthographicCamera((float)(map.getHeight() * aspectRatio), (float)map.getHeight());
        cam.position.set((float)(map.getWidth()+ map.getHeight() * (aspectRatio - 1))/2, (float)map.getHeight() / 2, 0);
        cam.zoom = (float)ZOOMSTEP;

        effectiveMoveStep = cam.zoom * MOVESTEP;
        effectiveViewportWidth = cam.zoom * cam.viewportWidth;
        effectiveViewportHeight = cam.zoom * cam.viewportHeight;

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
                AIScreen aiScreen = new AIScreen(surveilance);
                aiScreen.addMap(map);
                this.surveilance.setScreen(aiScreen);
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
        Gdx.gl.glClearColor(0f, 0f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Setup the builder tools stage
        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);

        map.render(shapeRenderer);
        if(drawing)
            drawingArea.render(shapeRenderer);

        //Add extra buttons
        stage.addActor(runGame);

        //Getting the components from the BuildTools
        stage.addActor(builderTools.getTable());

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
    private boolean drawing = false,colliding;
    private final double ZOOMSTEP = 1.1, ZOOMMIN = 0.1, ZOOMMAX = 1.3, MOVESTEP = 8;
    private double effectiveMoveStep, effectiveViewportWidth, effectiveViewportHeight;

    class mouseListener implements InputProcessor
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            if (screenX < 800 && AreaFactory.getAreaType() != null)
            {
                startPoint = transformScreenInput(screenX,Gdx.graphics.getHeight() - screenY);
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
                endPoint = transformScreenInput(screenX,Gdx.graphics.getHeight() - screenY);
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
            if(keycode == Input.Keys.UP)
            {
                if(cam.position.y + MOVESTEP * cam.zoom <= map.getHeight())
                    cam.translate(0f, (float)effectiveMoveStep);
                else
                    cam.position.y = (float)map.getHeight();
            }
            else if(keycode == Input.Keys.DOWN)
            {
                if(cam.position.y - MOVESTEP * cam.zoom >= 0)
                    cam.translate(0f,-(float)effectiveMoveStep);
                else
                    cam.position.y = 0;
            }
            else if(keycode == Input.Keys.LEFT)
            {
                if(cam.position.x - MOVESTEP * cam.zoom >= 0)
                    cam.translate(-(float)effectiveMoveStep,0f);
                else
                    cam.position.x = 0;
            }
            else if(keycode == Input.Keys.RIGHT)
            {
                if(cam.position.x + MOVESTEP * cam.zoom <= map.getWidth())
                    cam.translate((float)effectiveMoveStep,0f);
                else
                    cam.position.x = (float)map.getWidth();
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character)
        {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            if (drawing && drawingArea.inBounds(map.getWorld()) && drawingArea.getWidth() >= 0.8 && drawingArea.getHeight() >= 0.8)
            {
                colliding = false;

                for(Area area : map.getAreaList())
                    if(drawingArea.intersects(area))
                        colliding = true;

                if(!colliding)
                    map.addArea(drawingArea);
            }
            drawing = false;

            return false;
        }

        @Override
        public boolean scrolled(int amount)
        {
            if(amount > 0) // +1 is zoom out, -1 is zoom in
            {
                if (cam.zoom * ZOOMSTEP <= ZOOMMAX)
                    betterZoom(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), ZOOMSTEP);
            }
            else
            {
                if (cam.zoom / ZOOMSTEP >= ZOOMMIN)
                    betterZoom(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1 / ZOOMSTEP);
            }

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
        stage.dispose();
    }

    public void betterZoom(double screenX, double screenY, double zoomAmount)
    {
        double zoomDifference = cam.zoom * (zoomAmount - 1);

        cam.position.x += cam.viewportWidth * zoomDifference * ( 0.5 - screenX / Gdx.graphics.getWidth());
        cam.position.y += cam.viewportHeight * zoomDifference * ( 0.5 - screenY / Gdx.graphics.getHeight());

        cam.zoom *= zoomAmount;

        effectiveMoveStep = cam.zoom * MOVESTEP;
        effectiveViewportWidth = cam.zoom * cam.viewportWidth;
        effectiveViewportHeight = cam.zoom * cam.viewportHeight;
    }

    public double[] transformScreenInput(double screenX, double screenY)
    {
        double[] worldCoordinates = new double[2];

        worldCoordinates[0] = cam.position.x - (effectiveViewportWidth / 2) + screenX * effectiveViewportWidth / Gdx.graphics.getWidth();
        worldCoordinates[1] = cam.position.y - (effectiveViewportHeight / 2) + screenY * effectiveViewportHeight / Gdx.graphics.getHeight();

        return worldCoordinates;
    }
}
