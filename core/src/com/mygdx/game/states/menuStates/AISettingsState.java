package com.mygdx.game.states.menuStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.states.visualStates.SimulationState;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

public class AISettingsState extends MenuState
{
    private Stage stage;
    private Map map;
    private static Settings settings;

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

    public float nbGuards, typeGuards, nbIntruders, typeIntruders, maxTime, explorationTime, timeWeight, movementWeight,
                directComm, indirectComm;
    Settings gameSettings = new Settings();


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
        guardSB.setItems(GuardAI.GuardAIType.values());
        centerTable.add(guardSB).pad(5).row();
        guardSB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setGuardAIType((GuardAI.GuardAIType)guardSB.getSelected());
            }

        });
        guardAmountL = new Label("Number of Guards:", StateManager.skin);
        centerTable.add(guardAmountL).pad(5);
        guardAmountTF = new TextField("", StateManager.skin);
        centerTable.add(guardAmountTF).pad(5).row();
        guardAmountTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!guardAmountTF.equals(""))
                    settings.setGuardAmount((int) Float.parseFloat(guardAmountTF.getText()));
            }
        });

        intruderL = new Label("Intruder agents:", StateManager.skin);
        centerTable.add(intruderL).pad(5);
        intruderSB = new SelectBox(StateManager.skin);
        intruderSB.setItems(IntruderAI.IntruderAIType.values());
        centerTable.add(intruderSB).pad(5).row();
        intruderSB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setIntruderAIType((IntruderAI.IntruderAIType)intruderSB.getSelected());
            }
        });

        intruderAmountL = new Label("Number of Intruders:", StateManager.skin);
        centerTable.add(intruderAmountL).pad(5);
        intruderAmountTF = new TextField("", StateManager.skin);
        centerTable.add(intruderAmountTF).pad(5).row();
        intruderAmountTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!intruderAmountTF.equals(""))
                    settings.setIntruderAmount((int) Float.parseFloat(intruderAmountTF.getText()));
            }
        });

        timeL = new Label("Time Settings", StateManager.skin);
        timeL.setFontScale(1.3f);
        centerTable.add(timeL).pad(5).row();

        maxTimeL = new Label("Simulation Time", StateManager.skin);
        centerTable.add(maxTimeL).pad(5);
        maxTimeTF = new TextField("", StateManager.skin);
        centerTable.add(maxTimeTF).pad(5).row();
        maxTimeTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!maxTimeTF.equals(""))
                    settings.setMaxTime((int) Float.parseFloat(maxTimeTF.getText()));
            }
        });
        explorationPhaseL = new Label("Exploration Phase", StateManager.skin);
        centerTable.add(explorationPhaseL).pad(5);
        explorationPhaseB = new CheckBox("",StateManager.skin);
        centerTable.add(explorationPhaseB).pad(5).row();
        explorationPhaseB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setExplorationPhase(explorationPhaseB.isChecked());
            }
        });

        explorationTimeL =  new Label("Exploration Time", StateManager.skin);
        centerTable.add(explorationTimeL).pad(5);
        explorationTimeTF = new TextField("", StateManager.skin);
        centerTable.add(explorationTimeTF).pad(5).row();
        explorationTimeTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!explorationTimeTF.equals(""))
                    settings.setExplorationTime((int) Float.parseFloat(explorationTimeTF.getText()));
            }
        });
        weightsL = new Label("Performance weights", StateManager.skin);
        weightsL.setFontScale(1.3f);
        centerTable.add(weightsL).pad(5).row();

        timeWeightL = new Label("Time", StateManager.skin);
        centerTable.add(timeWeightL).pad(5);
        timeWeightTF = new TextField("", StateManager.skin);
        centerTable.add(timeWeightTF).pad(5).row();
        timeWeightTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!timeWeightTF.equals(""))
                    settings.setTimeWeight((int) Float.parseFloat(timeWeightTF.getText()));
            }
        });
        movementWeightL = new Label("Movement", StateManager.skin);
        centerTable.add(movementWeightL).pad(5);
        movementWeightTF = new TextField("", StateManager.skin);
        centerTable.add(movementWeightTF).pad(5).row();
        movementWeightTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!movementWeightTF.equals(""))
                    settings.setMovementWeight((int) Float.parseFloat(movementWeightTF.getText()));
            }
        });

        directCommL = new Label("Direct Communication", StateManager.skin);
        centerTable.add(directCommL).pad(5);
        directCommTF = new TextField("", StateManager.skin);
        centerTable.add(directCommTF).pad(5).row();
        directCommTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!directCommTF.equals(""))
                    settings.setDirectComWeight((int) Float.parseFloat(directCommTF.getText()));
            }
        });
        indirectCommL = new Label("Indirect Communication", StateManager.skin);
        centerTable.add(indirectCommL).pad(5);
        indirectCommTF = new TextField("", StateManager.skin);
        centerTable.add(indirectCommTF).pad(5).row();
        indirectCommTF.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(!indirectCommTF.equals(""))
                    settings.setIndirectComWeight((int) Float.parseFloat(indirectCommTF.getText()));
            }
        });
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

    public static Settings getSettings(){
        return settings;
    }

}
