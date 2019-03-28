package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.Screens.AIScreen;
import com.mygdx.game.SurveilanceSystem;

public class BuilderTools
{
    private Screen screen;

    private Table table;
    private Texture background;
    private TextButton structurebtn, sentryTowerbtn, shadebtn, targetbtn, runbtn;
    private Map map;


    public BuilderTools(SurveilanceSystem surveilance, Screen screen)
    {
        this.screen = screen;

        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));
        this.table = new Table();
        this.background = new Texture("core/assets/GreyArea.png");

        this.structurebtn = new TextButton("Structure", skin);
        this.sentryTowerbtn = new TextButton("Sentry Tower", skin);
        this.shadebtn = new TextButton("Shade", skin);
        this.targetbtn = new TextButton("Target", skin);
        this.runbtn = new TextButton("Run", skin);

        this.structurebtn.setPosition(Gdx.graphics.getWidth() - 200 + 10,Gdx.graphics.getHeight()-50);
        this.sentryTowerbtn.setPosition(Gdx.graphics.getWidth() - 200 + 10,Gdx.graphics.getHeight()-100);
        this.shadebtn.setPosition(Gdx.graphics.getWidth() - 200 + 10, Gdx.graphics.getHeight()-150);
        this.targetbtn.setPosition(Gdx.graphics.getWidth() - 200 + 10, Gdx.graphics.getHeight()-200);
        this.runbtn.setPosition(Gdx.graphics.getWidth() - 200 + 10, Gdx.graphics.getHeight()-250);

        this.structurebtn.setSize(180,40);
        this.sentryTowerbtn.setSize(180,40);
        this.shadebtn.setSize(180,40);
        this.targetbtn.setSize(180,40);
        this.runbtn.setSize(180,40);

        class StructureButtonListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                com.mygdx.game.Areas.AreaFactory.setAreaType(Area.AreaType.STRUCTURE);
            }
        }
        this.structurebtn.addListener(new StructureButtonListener());

        class SentryTowerListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                com.mygdx.game.Areas.AreaFactory.setAreaType(Area.AreaType.SENTRYTOWER);
            }
        }
        this.sentryTowerbtn.addListener(new SentryTowerListener());

        class ShadeListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                com.mygdx.game.Areas.AreaFactory.setAreaType(Area.AreaType.VEGETATION);
            }
        }
        this.shadebtn.addListener(new ShadeListener());

        class TargetListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                com.mygdx.game.Areas.AreaFactory.setAreaType(Area.AreaType.TARGET);
            }
        }
        this.targetbtn.addListener(new TargetListener());

        class RunListener extends ChangeListener
        {
            SurveilanceSystem surveilance;
            Screen screen;

            public RunListener(SurveilanceSystem surveilance, Screen screen) {
                this.surveilance = surveilance;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                AIScreen aiScreen = new AIScreen(surveilance);
                aiScreen.addMap(map);
                this.surveilance.setScreen(aiScreen);
//                this.screen.dispose();
            }
        }
        this.runbtn.addListener(new RunListener(surveilance,screen));

        this.table.background(new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                batch.draw(background,Gdx.graphics.getWidth()-200,0,200,Gdx.graphics.getHeight());
            }

            @Override
            public float getLeftWidth() {
                return 0;
            }

            @Override
            public void setLeftWidth(float leftWidth) {

            }

            @Override
            public float getRightWidth() {
                return 0;
            }

            @Override
            public void setRightWidth(float rightWidth) {

            }

            @Override
            public float getTopHeight() {
                return 0;
            }

            @Override
            public void setTopHeight(float topHeight) {

            }

            @Override
            public float getBottomHeight() {
                return 0;
            }

            @Override
            public void setBottomHeight(float bottomHeight) {

            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public void setMinWidth(float minWidth) {

            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public void setMinHeight(float minHeight) {

            }
        });
        this.table.addActor(structurebtn);
        this.table.addActor(sentryTowerbtn);
        this.table.addActor(shadebtn);
        this.table.addActor(targetbtn);
        this.table.addActor(runbtn);
    }

    public Table getTable() { return table;}

    public void setMap(Map map)
    {
        this.map = map;
    }
}
