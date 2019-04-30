package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.StateManager;
import com.mygdx.game.agents.ai.guard.GuardAI;
import com.mygdx.game.agents.ai.intruder.IntruderAI;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.states.visualStates.SimulationState;

public class AISettingsState extends MenuState
{
    private Stage stage;
    private Map map;
    private Settings settings;

    public AISettingsState(StateManager stateManager, Map map)
    {
        super(stateManager);
        this.map = map;
    }

    @Override
    public void create()
    {
        settings = new Settings();

        super.create();
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

    private Table table, topTable, centerTable, bottomTable;
    private Label titleL, agentsL, guardL, guardAmountL, intruderL, intruderAmountL;
    private SelectBox guardSB, intruderSB;
    private TextField guardAmountTF, intruderAmountTF;
    private TextButton okB, cancelB;

    @Override
    protected void createStage()
    {
        stage = new Stage(new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Main table
        table = new Table();
        table.setFillParent(true);

        //Top table
        topTable = new Table();

        titleL = new Label("agents Settings", StateManager.skin, "title");
        topTable.add(titleL).height(100f);

        table.add(topTable).expandX().top().row();

        //Center table
        centerTable = new Table();
        centerTable.columnDefaults(0).left().width(200f);
        centerTable.columnDefaults(1).right().width(300f);

        agentsL = new Label("Agents", StateManager.skin);
        centerTable.add(agentsL).row();

        guardL = new Label("Guard agents:", StateManager.skin);
        centerTable.add(guardL);

        guardSB = new SelectBox(StateManager.skin);
        guardSB.setItems(GuardAI.AIType.values());
        centerTable.add(guardSB).row();

        guardAmountL = new Label("Number of Guards:", StateManager.skin);
        centerTable.add(guardAmountL);

        guardAmountTF = new TextField("", StateManager.skin);
        centerTable.add(guardAmountTF).row();

        intruderL = new Label("Intruder agents:", StateManager.skin);
        centerTable.add(intruderL);

        intruderSB = new SelectBox(StateManager.skin);
        intruderSB.setItems(IntruderAI.AIType.values());
        centerTable.add(intruderSB).row();

        intruderAmountL = new Label("Number of Intruders:", StateManager.skin);
        centerTable.add(intruderAmountL);

        intruderAmountTF = new TextField("", StateManager.skin);
        centerTable.add(intruderAmountTF).row();

        table.add(centerTable).expand().top().row();

        //Bottom table
        bottomTable = new Table();
        bottomTable.right().pad(20f);

        okB = new TextButton("OK", StateManager.skin);
        okB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                stateManager.push(new SimulationState(stateManager, settings, map));
            }
        });
        bottomTable.add(okB).width(100f);

        cancelB = new TextButton("Cancel", StateManager.skin);
        cancelB.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                stateManager.pop();
            }
        });
        bottomTable.add(cancelB).width(100f);

        table.add(bottomTable).expandX().right().bottom();

        stage.addActor(table);
    }
}
