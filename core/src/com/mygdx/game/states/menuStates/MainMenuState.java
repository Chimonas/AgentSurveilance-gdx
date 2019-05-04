package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.StateManager;
import com.mygdx.game.states.visualStates.BuilderState;

public class MainMenuState extends MenuState
{
    private Stage stage;

    public MainMenuState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    @Override
    protected Stage getStage()
    {
        return stage;
    }

    private Label titleL;
    private TextButton newWorldB, loadWorldB;

    @Override
    protected void createStage()
    {
        stage = new Stage(new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        titleL = new Label("Agent Surveillance", StateManager.skin, "title");
        table.add(titleL).center().top().height(100f);
        table.row();

        newWorldB = new TextButton("New World", StateManager.skin, "default");
        newWorldB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stateManager.push(new BuilderState(stateManager));
            }
        });
        table.add(newWorldB).center().width(300);
        table.row();

        loadWorldB = new TextButton("Load World", StateManager.skin, "default");
        loadWorldB.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                stateManager.push(new AISettingsState(stateManager, new Map())); //loadworldstate
            }
        });
        table.add(loadWorldB).center().width(300);

        table.pad(50f);

        stage.addActor(table);
    }


}
