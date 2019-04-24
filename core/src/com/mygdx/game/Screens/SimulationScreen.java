//package com.mygdx.game.Screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.mygdx.game.agents.Agent;
//import com.mygdx.game.agents.Guard;
//import com.mygdx.game.agents.Intruder;
//import com.mygdx.game.areas.Area;
//import com.mygdx.game.gamelogic.Map;
//import com.mygdx.game.gamelogic.Settings;
//import com.mygdx.game.Physics.PhysicsEngine;
//import com.mygdx.game.SurveilanceSystem;
//
//import java.util.ArrayList;
//
//public class SimulationScreen implements Screen{
//    private SpriteBatch batch;
//    private Stage stage;
//
//    private OrthographicCamera cam;
//    private World world;
//    private Map map;
////    private Box2DDebugRenderer debugRenderer;
//    ShapeRenderer shapeRenderer = new ShapeRenderer();
//
//    //The amount of guards and intruders will be determined in the settings class
//    //THIS IS JUST A TEST
//    private int nbGuards = Settings.getGuardAmount();
//    private int nbIntruders = Settings.getIntruderAmount();
//    private Guard[] guards = new Guard[nbGuards];
//    private Intruder[] intruders = new Intruder[nbIntruders];
//    private Agent[] agents = new Agent[nbGuards]; //Gotta update for intruders to be included
//
//    final SurveilanceSystem surveilance;
//
//    public SimulationScreen(SurveilanceSystem surveilance){
//        this.surveilance = surveilance;
//        batch = new SpriteBatch();
//        stage = new Stage();
//        Gdx.input.setInputProcessor(stage);
//    }
//
//    @Override
//    public void show() {
//        if(map==null)
//            map = new Map();
//
//        batch = new SpriteBatch();
//
//        ShapeRenderer shapeRenderer = new ShapeRenderer();
//
//        //box2D
////        world = new World(Vector2.Zero, true);
////        debugRenderer = new Box2DDebugRenderer();
////        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//        double aspectRatio = (double)Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
//
//        cam = new OrthographicCamera((float)(map.getHeight() * aspectRatio), (float)map.getHeight());
//        cam.position.set((float)map.getWidth() / 2, (float)map.getHeight() / 2, 0);
//
//
//        //Should be created for each agents
//        //Body definition for the agents
//        BodyDef agentDef = new BodyDef();
//        agentDef.type = BodyDef.BodyType.DynamicBody;
//        agentDef.position.set(0,0);
//
//        System.out.println(map);
//
//        createRandomAgents(map.getAreaList());
//
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 1, 0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
////        debugRenderer.render(world, cam.combined);
//
//        batch.begin();
//
//        cam.update();
//        batch.setProjectionMatrix(cam.combined);
//
//        shapeRenderer.setProjectionMatrix(cam.combined);
//
//        map.render(shapeRenderer);
//
//        for(Agent a: agents){
//
////            batch.draw(a.getBody().getTexture(),
////                        (float) a.getBody().getPosition()[0],
////                        (float) a.getBody().getPosition()[1]);
//
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle((float)(a.getBody().getPosition()[0]), (float)a.getBody().getPosition()[1],(float)1);
//            shapeRenderer.end();
//
//            a.getBody().randomMovement();
//
//            a.update(map.getAreaList());
//
//            //If the agents goes out of screen, he HAS to come back
//            if(a.getBody().getPosition()[0] < 0) a.getBody().setXPosition(0);
//            if(a.getBody().getPosition()[0] > map.getWidth()) a.getBody().setXPosition(map.getWidth());
//            if(a.getBody().getPosition()[1] < 0) a.getBody().setYPosition(0);
//            if(a.getBody().getPosition()[1] > map.getHeight()) a.getBody().setYPosition(map.getHeight());
//        }
//
//        batch.end();
//        stage.act();
//        stage.draw();
//
//
//        InputProcessor mouseListener = new mouseListener();
//        Gdx.input.setInputProcessor(mouseListener);
//    }
//
//    private final double ZOOMSTEP = 1.1, ZOOMMIN = 0.1, ZOOMMAX = 100, MOVESTEP = 8;
//    private double effectiveMoveStep, effectiveViewportWidth, effectiveViewportHeight;
//
//    class mouseListener implements InputProcessor
//    {
//        @Override
//        public boolean touchDown(int screenX, int screenY, int pointer, int button)
//        {
//            return false;
//        }
//
//        @Override
//        public boolean touchDragged(int screenX, int screenY, int pointer)
//        {
//            return false;
//        }
//
//        @Override
//        public boolean mouseMoved(int screenX, int screenY) {
//            return false;
//        }
//
//    public void createRandomAgents(ArrayList<Area> areas){
//        //Temporary creation of agents with random initial positions
//        for(int i = 0; i < Settings.getGuardAmount(); i++){
//
//            while(agents[i] == null){
//                double [] pos = generateRandomPosition();
//                double ang = Math.random()*360;
//                if(!PhysicsEngine.checkInStructure(areas, pos)){
//                    guards[i] = new Guard(pos, (float) ang);
//                    agents[i] = guards[i];
//                }
//            }
//        }
//    }
//
//    public double[] generateRandomPosition(){
//        double [] pos = {Math.random()*map.getWidth(), Math.random()*map.getHeight()};
//        return pos;
//    }
//
//
//
//}
