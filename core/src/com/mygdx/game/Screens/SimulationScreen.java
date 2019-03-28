package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.AI.Agent;
import com.mygdx.game.AI.Guard;
import com.mygdx.game.AI.Intruder;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.GameLogic.Map;
import com.mygdx.game.GameLogic.Settings;
import com.mygdx.game.Physics.PhysicsEngine;
import com.mygdx.game.SurveilanceSystem;

import java.util.ArrayList;

public class SimulationScreen implements Screen{
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera cam;
    private World world;
    private Map map;
//    private Box2DDebugRenderer debugRenderer;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    //The amount of guards and intruders will be determined in the settings class
    //THIS IS JUST A TEST
    private int nbGuards = Settings.getGuardAmount();
    private int nbIntruders = Settings.getIntruderAmount();
    private Guard[] guards = new Guard[nbGuards];
    private Intruder[] intruders = new Intruder[nbIntruders];
    private Agent[] agents = new Agent[nbGuards]; //Gotta update for intruders to be included

    final SurveilanceSystem surveilance;

    public SimulationScreen(SurveilanceSystem surveilance){
        this.surveilance = surveilance;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        if(map==null)
            map = new Map();

        batch = new SpriteBatch();

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        //box2D
//        world = new World(Vector2.Zero, true);
//        debugRenderer = new Box2DDebugRenderer();
//        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        double aspectRatio = (double)Gdx.graphics.getWidth()/Gdx.graphics.getHeight();

        cam = new OrthographicCamera((float)(map.getHeight() * aspectRatio), (float)map.getHeight());
        cam.position.set((float)map.getWidth() / 2, (float)map.getHeight() / 2, 0);


        //Should be created for each agent
        //Body definition for the agent
        BodyDef agentDef = new BodyDef();
        agentDef.type = BodyDef.BodyType.DynamicBody;
        agentDef.position.set(0,0);

        System.out.println(map);

        createRandomAgents(map.getAreaList());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        debugRenderer.render(world, cam.combined);

        batch.begin();

        cam.update();
        batch.setProjectionMatrix(cam.combined);

        shapeRenderer.setProjectionMatrix(cam.combined);

        map.render(shapeRenderer);

        for(Agent a: agents){

//            batch.draw(a.getBody().getTexture(),
//                        (float) a.getBody().getPosition()[0],
//                        (float) a.getBody().getPosition()[1]);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle((float)(a.getBody().getPosition()[0]), (float)a.getBody().getPosition()[1],(float)1);
            shapeRenderer.end();

            a.getBody().randomMovement();

            a.update(map.getAreaList());

            //If the agent goes out of screen, he HAS to come back
            if(a.getBody().getPosition()[0] < 0) a.getBody().setXPosition(0);
            if(a.getBody().getPosition()[0] > map.getWidth()) a.getBody().setXPosition(map.getWidth());
            if(a.getBody().getPosition()[1] < 0) a.getBody().setYPosition(0);
            if(a.getBody().getPosition()[1] > map.getHeight()) a.getBody().setYPosition(map.getHeight());
        }

        batch.end();
        stage.act();
        stage.draw();


        InputProcessor mouseListener = new mouseListener();
        Gdx.input.setInputProcessor(mouseListener);
    }

    private final double ZOOMSTEP = 1.1, ZOOMMIN = 0.1, ZOOMMAX = 100, MOVESTEP = 8;
    private double effectiveMoveStep, effectiveViewportWidth, effectiveViewportHeight;

    class mouseListener implements InputProcessor
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
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
    public void dispose () {
        batch.dispose();
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

    public void addMap(Map map){
        this.map = map;
    }

    public void createRandomAgents(ArrayList<Area> areas){
        //Temporary creation of agents with random initial positions
        for(int i = 0; i < Settings.getGuardAmount(); i++){

            while(agents[i] == null){
                double [] pos = generateRandomPosition();
                double ang = Math.random()*360;
                if(!PhysicsEngine.checkInStructure(areas, pos)){
                    guards[i] = new Guard(pos, (float) ang);
                    agents[i] = guards[i];
                }
            }


        }
    }

    public double[] generateRandomPosition(){
        double [] pos = {Math.random()*map.getWidth(), Math.random()*map.getHeight()};
        return pos;
    }



}
