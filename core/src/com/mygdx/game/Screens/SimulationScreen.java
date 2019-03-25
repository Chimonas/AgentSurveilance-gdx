package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
    private Box2DDebugRenderer debugRenderer;
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

        batch = new SpriteBatch();

        //box2D
        world = new World(Vector2.Zero, true);
        debugRenderer = new Box2DDebugRenderer();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Should be created for each agent
        //Body definition for the agent
        BodyDef agentDef = new BodyDef();
        agentDef.type = BodyDef.BodyType.DynamicBody;
        agentDef.position.set(0,0);

        createRandomAgents(map.getAreaList());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, cam.combined);

        batch.begin();

        if(map!=null) map.render(batch, shapeRenderer);
        else{
            map = new Map();
        }
        for(Agent a: agents){

            batch.draw(a.getBody().getTexture(),
                        (float) a.getBody().getPosition()[0],
                        (float) a.getBody().getPosition()[1]);
            a.getBody().randomMovement();
            a.update(map.getAreaList());

            //If the agent goes out of screen, he HAS to come back
            if(a.getBody().getPosition()[0] < 0) a.getBody().setXPosition(0);
            if(a.getBody().getPosition()[0] > Gdx.graphics.getWidth() - 20) a.getBody().setXPosition(Gdx.graphics.getWidth() - 20);
            if(a.getBody().getPosition()[1] < 0) a.getBody().setYPosition(0);
            if(a.getBody().getPosition()[1] > Gdx.graphics.getHeight() - 20) a.getBody().setYPosition(Gdx.graphics.getHeight() - 20);

        }

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

    public void createRandomAgents(ArrayList<Area> areas){
        //Temporary creation of agents with random initial positions
        for(int i = 0; i < Settings.getGuardAmount(); i++){

            double posX = Math.random()*Gdx.graphics.getWidth();
            double posY = Math.random()*Gdx.graphics.getHeight();
            double[] pos = {posX, posY};
            double ang = Math.random()*360;


            //Not fully working.... to be checked more in detail
            if(!PhysicsEngine.inStructure(areas, pos, pos)){
                guards[i] = new Guard(pos, (float) ang);
                agents[i] = guards[i];
            }

        }
    }

}
