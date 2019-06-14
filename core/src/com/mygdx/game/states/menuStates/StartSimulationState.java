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
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.states.visualStates.SimulationState;

public class StartSimulationState extends MenuState {

    private Stage stage;
    private static final float BUTTONWIDTH = 300;
    public World world;

    public StartSimulationState(StateManager stateManager, World world) {
        super(stateManager);
        this.world = world;
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
    protected Stage getStage() {
        return stage;
    }

    private Label messageL;
    private TextButton continueB;
    private TextButton cancelB;
    private TextButton statsB;

    @Override
    protected void createStage() {


        this.world.setSimulationStartTick(this.world.getGameLoop().getTicks());
        stage = new Stage((new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        messageL = new Label("The Exploration Phase is over.", StateManager.skin, "title");
        table.add(messageL). center().top().height(100f);
        table.row();

        continueB = new TextButton("Start Simulation", StateManager.skin, "default");
        continueB.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                stateManager.pop();
//                stateManager.push(new SimulationState(stateManager, world.getSettings(), world.getMap()));
            }
        } );
        table.add(continueB).center().width(BUTTONWIDTH);
        table.row();

        cancelB = new TextButton("Cancel", StateManager.skin, "default");
        cancelB.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                stateManager.push(new MainMenuState(stateManager));
            }
        });
        table.add(cancelB).center().width(BUTTONWIDTH);
        table.row();

        statsB = new TextButton("Get Statistics", StateManager.skin, "default");
        statsB.addListener(new ClickListener(){
//            public void clicked(InputEvent event, float x, float y){
//                stateManager.push(new StatsState(stateManager));
//            }
        });
        table.add(statsB).center().width(BUTTONWIDTH);
        table.row();

        table.pad(50f);
        stage.addActor(table);

    }



}
