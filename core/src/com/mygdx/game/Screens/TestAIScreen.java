//package com.mygdx.game.Screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.Array;
//import com.mygdx.game.agents.Agent;
//import com.mygdx.game.gamelogic.Map;
//import com.mygdx.game.SurveilanceSystem;
//
//import java.awt.*;
//
//public class TestAIScreen implements Screen {
//
//    private Shape agents;
//    private OrthographicCamera cam;
//    private SpriteBatch batch;
//    private Array<Agent> guards;
//    private Array<Agent> intruders;
//
//    public TestAIScreen(SurveilanceSystem surveilanceSystem){
//
//        Map map = new Map();
//
//
//    }
//
//    @Override
//    public void show() {
//
//    }
//
//    @Override
//    public void render(float delta) {
//
//        //Should be the map built in BuilderScreen
//        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        cam.update();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//}
