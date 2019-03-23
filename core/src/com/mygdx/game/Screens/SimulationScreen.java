package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.AI.Agent;
import com.mygdx.game.AI.Guard;
import com.mygdx.game.AI.Intruder;
import com.mygdx.game.SurveilanceSystem;

public class SimulationScreen implements Screen{
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    //The amount of guards and intruders will be determined in the settings class
    private int nbGuards = 10;
    private int nbIntruders = 10;
    private Guard[] guards = new Guard[nbGuards];
    private Intruder[] intruders = new Intruder[nbIntruders];
    private Agent[] agents = new Agent[nbGuards]; //Gotta update for intruders to be included

    final SurveilanceSystem surveilance;

    public SimulationScreen(SurveilanceSystem surveilance){
        this.surveilance = surveilance;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Temporary creation of random agents
        for(int i = 0; i < nbGuards; i++){

            double posX = Math.random()*Gdx.graphics.getWidth();
            double posY = Math.random()*Gdx.graphics.getHeight();
            double[] pos = {posX, posY};
            double ang = Math.random()*360;

            guards[i] = new Guard(pos, (float) ang);
            agents[i] = guards[i];
        }

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

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, cam.combined);

        batch.begin();

        for(Agent a: agents){
            batch.draw(a.getBody().getTexture(),
                        (float) a.getBody().getPosition()[0],
                        (float) a.getBody().getPosition()[1]);
            a.update();
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
}
