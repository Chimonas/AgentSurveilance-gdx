package com.mygdx.game.GameLogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Areas.Area;

import java.util.ArrayList;

public class Map
{
    private Stage stage;
    private double width,height;
    private Actor mapActor;
    private AreaFactory factory;
    ArrayList<Area> areas;

    public Map(double width, double height)
    {
        factory = new AreaFactory();
        stage = new Stage();
        this.width = width;
        this.height = height;
        areas = new ArrayList<Area>();

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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Area a : areas) {

            shapeRenderer.setColor(Color.BLACK);
            Vector2 pointA = new Vector2(a.x,a.y);
            Vector2 pointB = new Vector2(a.x,a.y+a.height);
            Vector2 pointC = new Vector2(a.x+a.width,a.y);
            Vector2 pointD = new Vector2(a.x+a.width,a.y+a.height);
            shapeRenderer.line(pointA,pointB);
            shapeRenderer.line(pointB,pointD);
            shapeRenderer.line(pointA,pointC);
            shapeRenderer.line(pointC,pointD);
        }
        shapeRenderer.end();

        batch.begin();
    }

    public void addArea(int startX, int startY, int endX, int endY) {
        areas.add(factory.addArea(startX,startY,endX,endY));
    }

    public Stage getStage() {
        return stage;
    }

}
