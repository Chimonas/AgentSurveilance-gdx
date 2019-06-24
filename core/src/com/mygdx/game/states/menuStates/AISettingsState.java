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
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

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
    private Label titleL, agentsL, guardL, explorationL, guardAmountL, intruderL, intruderAmountL, timeL, maxTimeL, explorationPhaseL,
            explorationTimeL, simulationAmountL;
    private SelectBox guardSB, explorationSB, intruderSB;
    private TextField guardAmountTF, intruderAmountTF, maxTimeTF, explorationTimeTF, simulationAmountTF;
    private TextButton simulateB, okB, cancelB;
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
        guardSB.setItems(GuardAI.GuardAIType.values());
        centerTable.add(guardSB).pad(5).row();

        explorationL = new Label("Exploration agents:", StateManager.skin);
        centerTable.add(explorationL).pad(5);
        explorationSB = new SelectBox(StateManager.skin);
        explorationSB.setItems(ExplorationAI.ExplorationAIType.values());
        centerTable.add(explorationSB).pad(5).row();

        guardAmountL = new Label("Number of Guards:", StateManager.skin);
        centerTable.add(guardAmountL).pad(5);
        guardAmountTF = new TextField("", StateManager.skin);
        centerTable.add(guardAmountTF).pad(5).row();

        stage.setKeyboardFocus(guardAmountTF);

        intruderL = new Label("Intruder agents:", StateManager.skin);
        centerTable.add(intruderL).pad(5);
        intruderSB = new SelectBox(StateManager.skin);
        intruderSB.setItems(IntruderAI.IntruderAIType.values());
        centerTable.add(intruderSB).pad(5).row();

        intruderAmountL = new Label("Number of Intruders:", StateManager.skin);
        centerTable.add(intruderAmountL).pad(5);
        intruderAmountTF = new TextField("", StateManager.skin);
        centerTable.add(intruderAmountTF).pad(5).row();

        timeL = new Label("Time Settings", StateManager.skin);
        timeL.setFontScale(1.3f);
        centerTable.add(timeL).pad(5).row();

        maxTimeL = new Label("Simulation Time (s)", StateManager.skin);
        centerTable.add(maxTimeL).pad(5);
        maxTimeTF = new TextField("", StateManager.skin);
        centerTable.add(maxTimeTF).pad(5).row();

        explorationPhaseL = new Label("Exploration Phase", StateManager.skin);
        centerTable.add(explorationPhaseL).pad(5);
        explorationPhaseB = new CheckBox("",StateManager.skin);
        centerTable.add(explorationPhaseB).pad(5).row();
        explorationPhaseB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                explorationTimeTF.setDisabled(!explorationPhaseB.isChecked());
            }
        });

        explorationTimeL =  new Label("Exploration Time (s)", StateManager.skin);
        centerTable.add(explorationTimeL).pad(5);
        explorationTimeTF = new TextField("", StateManager.skin);
        centerTable.add(explorationTimeTF).pad(5).row();
        explorationTimeTF.setDisabled(true);

        simulationAmountL = new Label("Number of simulations", StateManager.skin);
        centerTable.add(simulationAmountL).pad(5);
        simulationAmountTF = new TextField("", StateManager.skin);
        centerTable.add(simulationAmountTF).pad(5).row();

        table.add(centerTable).expand().top().row();

        //Bottom table
        bottomTable = new Table();
        bottomTable.right().pad(20f);

        simulateB = new TextButton("Simulate", StateManager.skin);
        simulateB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(setSettings())
                {
                    if(simulationAmountTF.getText().isEmpty())
                    {
                        stage.setKeyboardFocus(simulationAmountTF);
                        return;
                    }
                    else
                    {
                        try {
                            settings.setSimulationAmount((int)Float.parseFloat(simulationAmountTF.getText()));
                        }
                        catch (NumberFormatException e) {
                            stage.setKeyboardFocus(guardAmountTF);
                            return;
                        }
                    }

                    settings.setMultipleSimulations(true);
                    stateManager.push(new GraphiclessSimulationState(stateManager, settings, map));
                }
            }
        });
        bottomTable.add(simulateB).width(100f);

        okB = new TextButton("OK", StateManager.skin);
        okB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(setSettings())
                {
                    settings.setMultipleSimulations(false);
                    stateManager.push(new SimulationState(stateManager, settings, map));
                }
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

    public boolean setSettings()
    {
        settings.setGuardAIType((GuardAI.GuardAIType)guardSB.getSelected());
        settings.setExplorationAIType((ExplorationAI.ExplorationAIType)explorationSB.getSelected());
        settings.setIntruderAIType((IntruderAI.IntruderAIType)intruderSB.getSelected());

        if(guardAmountTF.getText().isEmpty())
            settings.setGuardAmount(0);
        else
        {
            try {
                settings.setGuardAmount((int) Float.parseFloat(guardAmountTF.getText()));
            }
            catch (NumberFormatException e) {
                stage.setKeyboardFocus(guardAmountTF);
                return false;
            }
        }

        if(intruderAmountTF.getText().isEmpty())
            settings.setIntruderAmount(0);
        else
        {
            try{
                settings.setIntruderAmount((int) Float.parseFloat(intruderAmountTF.getText()));
            } catch(NumberFormatException e){
                stage.setKeyboardFocus(intruderAmountTF);
                return false;
            }
        }

        if(maxTimeTF.getText().isEmpty())
        {
            stage.setKeyboardFocus(maxTimeTF);
            return false;
        }
        else
        {
            try {
                settings.setMaxTime((int) Float.parseFloat(maxTimeTF.getText()));
            } catch (NumberFormatException e) {
                stage.setKeyboardFocus(maxTimeTF);
                return false;
            }
        }

        settings.setExplorationPhase(explorationPhaseB.isChecked());

        if(explorationPhaseB.isChecked())
        {
            if(explorationTimeTF.getText().isEmpty())
            {
                stage.setKeyboardFocus(explorationTimeTF);
                return false;
            }
            else
            {
                try {
                    settings.setExplorationTime((int) Float.parseFloat(explorationTimeTF.getText()));
                } catch(NumberFormatException e){
                    stage.setKeyboardFocus(explorationTimeTF);
                    return false;
                }
            }
        }

        return true;
    }

    public Settings getSettings(){
        return settings;
    }

}
