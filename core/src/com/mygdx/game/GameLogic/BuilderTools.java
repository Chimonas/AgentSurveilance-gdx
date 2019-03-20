package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class BuilderTools {

    private Stage stage;
    private TextButton structurebtn, sentryTowerbtn;

    public BuilderTools() {
        Skin skin = new Skin(Gdx.files.internal("core/assets/commodore64/skin/uiskin.json"));
        this.stage = new Stage();

        this.structurebtn = new TextButton("Structure", skin);
        this.sentryTowerbtn = new TextButton("Sentry Tower", skin);

        this.structurebtn.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-50);
        this.sentryTowerbtn.setPosition(Gdx.graphics.getWidth()*4/5 + 10,Gdx.graphics.getHeight()-100);

        this.structurebtn.setSize(180,40);
        this.sentryTowerbtn.setSize(180,40);

        class StructureButtonListener extends ChangeListener {

            public StructureButtonListener() {
                System.out.println("Listener created");
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AreaFactory.setAreaType(AreaFactory.STRUCTURE);
//                System.out.println("Wall button has been pressed");
            }
        }
        this.structurebtn.addListener(new StructureButtonListener());

        class SentryTowerListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                AreaFactory.setAreaType(AreaFactory.SENTRYTOWER);
//                System.out.println("Sentry tower button has been pressed");
            }
        }
        this.sentryTowerbtn.addListener(new SentryTowerListener());

        this.stage.addActor(structurebtn);
        this.stage.addActor(sentryTowerbtn);
    }

    public Stage getStage() {
        return stage;
    }
}
