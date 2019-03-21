package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Areas.Area;

import java.awt.event.ActionEvent;

public class BuilderTools {

    private Stage stage;
    private TextButton structurebtn, sentryTowerbtn;

    public BuilderTools() {
        Skin skin = new Skin(Gdx.files.internal("core/assets/cloud-form/skin/cloud-form-ui.json"));
        this.stage = new Stage();

        this.structurebtn = new TextButton("Structure", skin);
        this.sentryTowerbtn = new TextButton("Sentry Tower", skin);

        this.structurebtn.setPosition(Gdx.graphics.getWidth() - 200 + 10,Gdx.graphics.getHeight()-50);
        this.sentryTowerbtn.setPosition(Gdx.graphics.getWidth() - 200 + 10,Gdx.graphics.getHeight()-100);

        this.structurebtn.setSize(180,40);
        this.sentryTowerbtn.setSize(180,40);

        class StructureButtonListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                AreaFactory.setAreaType(Area.AreaType.STRUCTURE);
            }
        }
        this.structurebtn.addListener(new StructureButtonListener());

        class SentryTowerListener extends ChangeListener
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                AreaFactory.setAreaType(Area.AreaType.SENTRYTOWER);
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
