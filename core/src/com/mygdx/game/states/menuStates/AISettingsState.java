package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.states.visualStates.SimulationState;
import com.mygdx.game.worldAttributes.agents.ai.guard.GuardAI;
import com.mygdx.game.worldAttributes.agents.ai.intruder.IntruderAI;

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
    private Label titleL, agentsL, guardL, guardAmountL, intruderL, intruderAmountL, timeL, maxTimeL, explorationPhaseL,
            explorationTimeL, weightsL, timeWeightL, movementWeightL, directCommL, indirectCommL;
    private SelectBox guardSB, intruderSB;
    private TextField guardAmountTF, intruderAmountTF, maxTimeTF, explorationTimeTF, timeWeightTF, movementWeightTF,
            directCommTF, indirectCommTF;
    private TextButton okB, cancelB;
    private CheckBox explorationPhaseB;

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
        agentsL.setFontScale(1.3f);
        centerTable.add(agentsL).pad(5).row();

        guardL = new Label("Guard agents:", StateManager.skin);
        centerTable.add(guardL).pad(5);

        guardSB = new SelectBox(StateManager.skin);
        guardSB.setItems(GuardAI.AIType.values());
        centerTable.add(guardSB).pad(5).row();

        guardAmountL = new Label("Number of Guards:", StateManager.skin);
        centerTable.add(guardAmountL).pad(5);

        guardAmountTF = new TextField("", StateManager.skin);
        centerTable.add(guardAmountTF).pad(5).row();

        intruderL = new Label("Intruder agents:", StateManager.skin);
        centerTable.add(intruderL).pad(5);

        intruderSB = new SelectBox(StateManager.skin);
        intruderSB.setItems(IntruderAI.AIType.values());
        centerTable.add(intruderSB).pad(5).row();

        intruderAmountL = new Label("Number of Intruders:", StateManager.skin);
        centerTable.add(intruderAmountL).pad(5);

        intruderAmountTF = new TextField("", StateManager.skin);
        centerTable.add(intruderAmountTF).pad(5).row();


        timeL = new Label("Time Settings", StateManager.skin);
        timeL.setFontScale(1.3f);
        centerTable.add(timeL).pad(5).row();

        maxTimeL = new Label("Simulation Time", StateManager.skin);
        centerTable.add(maxTimeL).pad(5);

        maxTimeTF = new TextField("", StateManager.skin);
        centerTable.add(maxTimeTF).pad(5).row();

        explorationPhaseL = new Label("Exploration Phase", StateManager.skin);
        centerTable.add(explorationPhaseL).pad(5);

        explorationPhaseB = new CheckBox("",StateManager.skin);
        centerTable.add(explorationPhaseB).pad(5).row();

        explorationTimeL =  new Label("Exploration Time", StateManager.skin);
        centerTable.add(explorationTimeL).pad(5);

        explorationTimeTF = new TextField("", StateManager.skin);
        centerTable.add(explorationTimeTF).pad(5).row();


        weightsL = new Label("Performance weights", StateManager.skin);
        weightsL.setFontScale(1.3f);
        centerTable.add(weightsL).pad(5).row();

        timeWeightL = new Label("Time", StateManager.skin);
        centerTable.add(timeWeightL).pad(5);

        timeWeightTF = new TextField("", StateManager.skin);
        centerTable.add(timeWeightTF).pad(5).row();

        movementWeightL = new Label("Movement", StateManager.skin);
        centerTable.add(movementWeightL).pad(5);

        movementWeightTF = new TextField("", StateManager.skin);
        centerTable.add(movementWeightTF).pad(5).row();

        directCommL = new Label("Direct Communication", StateManager.skin);
        centerTable.add(directCommL).pad(5);

        directCommTF = new TextField("", StateManager.skin);
        centerTable.add(directCommTF).pad(5).row();

        indirectCommL = new Label("Indirect Communication", StateManager.skin);
        centerTable.add(indirectCommL).pad(5);

        indirectCommTF = new TextField("", StateManager.skin);
        centerTable.add(indirectCommTF).pad(5).row();

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
