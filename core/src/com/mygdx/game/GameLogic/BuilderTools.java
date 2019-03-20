package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Screens.BuilderScreen;
import com.mygdx.game.Screens.MenuScreen;

public class BuilderTools {

    private Stage stage;
    TextButton wallbtn, sentryTowerbtn, vegetationBtn,targetBtn, fileBtn ;

    public BuilderTools() {
        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));
        this.stage = new Stage();

        this.wallbtn = new TextButton("Wall", skin);
        this.sentryTowerbtn = new TextButton("Sentry Tower", skin);
        this.vegetationBtn = new TextButton("Vegetation",skin);
        this.targetBtn = new TextButton("Target Area", skin);
        this.fileBtn  = new TextButton("Load Map",skin);


        this.wallbtn.setPosition(Gdx.graphics.getWidth()*4/5  + 10,Gdx.graphics.getHeight()-50);
        this.sentryTowerbtn.setPosition(Gdx.graphics.getWidth()*4/5 -10 + 10,Gdx.graphics.getHeight()-100);
        this.vegetationBtn.setPosition(Gdx.graphics.getWidth()*4/5 +10, Gdx.graphics.getHeight()-150);
        this.targetBtn.setPosition(Gdx.graphics.getWidth()*4/5 , Gdx.graphics.getHeight()-200);
        this.fileBtn.setPosition(Gdx.graphics.getWidth()*4/5 +10, Gdx.graphics.getHeight()-700);


        this.wallbtn.setSize(180,40);
        this.sentryTowerbtn.setSize(200,40);
        this.vegetationBtn.setSize(180,40);
        this.targetBtn.setSize(200,40);
        this.fileBtn.setSize(180,40);



        class WallButtonListener extends ChangeListener {

            public WallButtonListener() {
                System.out.println("Listener created");
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AreaFactory.typeOfAreaOnMouseClick = "Wall";
                System.out.println("Wall button has been pressed");
            }
        }
        this.wallbtn.addListener(new WallButtonListener());

        class SentryTowerListener extends ChangeListener {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AreaFactory.typeOfAreaOnMouseClick = "Sentry Tower";
                System.out.println("Sentry tower button has been pressed");
            }
        }
        this.sentryTowerbtn.addListener(new SentryTowerListener());


        class VegetationListener extends ChangeListener {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                AreaFactory.typeOfAreaOnMouseClick = "Vegetation";
                System.out.println("Vegetation button has been pressed");

            }
        }
        this.vegetationBtn.addListener(new VegetationListener());

        class TargetListener extends ChangeListener {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                AreaFactory.typeOfAreaOnMouseClick = "Target Area";
                System.out.println("Target Area button has been pressed");

            }
        }
        this.targetBtn.addListener(new TargetListener());


        class FileListener extends ChangeListener{
            @Override
            public void changed(ChangeEvent event, Actor actor ){
                //TODO implement File Loader, display text files with properties of the maps that can be loaded into the "Builder"
                // environment as well as loaded in the "Running" environment.
                System.out.println("File Button has been clicked");

            }

        }
        this.fileBtn.addListener(new FileListener());



        this.stage.addActor(wallbtn);
        this.stage.addActor(sentryTowerbtn);
        this.stage.addActor(vegetationBtn);
        this.stage.addActor(targetBtn);
        this.stage.addActor(fileBtn);


    }

    public Stage getStage() {
        return stage;
    }



}
