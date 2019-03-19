package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class BuilderTools {

    private Stage stage;
    TextButton wallbtn, sentryTowerbtn;

    public BuilderTools() {
        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));
        this.stage = new Stage();

        this.wallbtn = new TextButton("Wall", skin);
        this.sentryTowerbtn = new TextButton("Sentry Tower", skin);

        this.wallbtn.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-50);
        this.sentryTowerbtn.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-100);

        this.wallbtn.setSize(180,40);
        this.sentryTowerbtn.setSize(180,40);

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


        this.stage.addActor(wallbtn);
        this.stage.addActor(sentryTowerbtn);
    }

    public Stage getStage() {
        return stage;
    }


}
