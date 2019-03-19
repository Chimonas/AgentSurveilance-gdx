package com.mygdx.game.GameLogic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MouseEvent;

import java.awt.geom.Area;
import java.util.ArrayList;

public class Map {

    private Stage stage;
    private double width,height;
    private Actor mapActor;
    private AreaFactory factory;
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();



    public Map(double width, double height ) {
        factory = new AreaFactory();
        stage = new Stage();
        this.width = width;
        this.height = height;

        //does nothing (for now)
//        mapActor = new Actor();
//        mapActor.setOrigin(250,0);
//        stage.addActor(mapActor);


    }

    public void render(Batch batch) {
        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        //To be updated:
        // rects should be an Area. Each area should contain:
        // 1. a rectangle(the area to be drawn that represents the shape)
        // 2. a texture for that area
        // Use factory design pattern (AreaFactory)

        for (Rectangle r : rects) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(r.x ,r.y,r.width,r.height);
            shapeRenderer.end();
        }

        batch.begin();

    }

    public void addArea(int startX, int startY, int endX, int endY) {
        rects.add(factory.addArea(startX,startY,endX,endY));
    }

    public Stage getStage() {
        return stage;
    }

}
