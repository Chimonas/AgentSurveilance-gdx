package com.mygdx.game.states.visualStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.AreaFactory;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.StateManager;
import com.mygdx.game.states.menuStates.AISettingsState;
import com.mygdx.game.states.visualStates.drawers.AreaDrawer;
import com.mygdx.game.states.visualStates.drawers.MapDrawer;

public class BuilderState extends VisualState
{
    private Area currentArea;
    private Vector2 startPoint, endPoint;
    private float gridSize;
    private boolean drawing, areaValid, showGrid;

    public BuilderState(StateManager stateManager, Map map)
    {
        super(stateManager, map);
    }

    public BuilderState(StateManager stateManager)
    {
        this(stateManager, new Map());
    }

    @Override
    public void create()
    {
        super.create();

        startPoint = new Vector2();
        endPoint = new Vector2();

        gridSize = 1.0f;

        drawing = false;
        areaValid = true;
        showGrid = false;

        createHud();
        hud.act();
        hud.draw();
    }

    @Override
    public void render()
    {
        super.render();

        MapDrawer.render(shapeRenderer, map,showGrid,gridSize);

        if(currentArea != null)
            AreaDrawer.render(shapeRenderer, currentArea, !areaValid, true);

        Gdx.input.setInputProcessor(new InputMultiplexer(hud,this));

        hud.act();
        hud.draw();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * Start the drawing of a new currentArea
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.LEFT)
        {
            if(AreaFactory.getAreaType() != null)
            {
                saveCurrentArea();

                startPoint.set(transformScreenInput(screenX,Gdx.graphics.getHeight() - screenY));

                if(showGrid)
                    snapToGrid(startPoint);

                currentArea = AreaFactory.newArea(startPoint);

                drawing = true;
            }
        }

        return super.touchDown(screenX,screenY,pointer,button);
    }

    /**
     * If drawing, update the position of currentArea
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        if(drawing)
        {
            endPoint.set(transformScreenInput(screenX,Gdx.graphics.getHeight() - screenY));

            if(showGrid)
                snapToGrid(endPoint);

            currentArea.setPosition(startPoint, endPoint);
            areaValid = currentArea.isValid(map);
        }

        return super.touchDragged(screenX,screenY,pointer);
    }

    /**
     * Stop the drawing of currentArea
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.LEFT)
        {
            drawing = false;
        }

        return super.touchUp(screenX,screenY,pointer,button);
    }

    /**
     * ENTER:   If not drawing, save the current area
     * G:       Toggle showGrid and update currentArea position
     */
    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ENTER)
        {
            if(!drawing)
                saveCurrentArea();
        }

        if(keycode == Input.Keys.G)
        {
            showGrid = !showGrid;

            if(drawing)
            {
                endPoint.set(transformScreenInput(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY()));
                if(showGrid)
                    snapToGrid(endPoint);

                currentArea.setPosition(startPoint, endPoint);
            }
        }

        return super.keyDown(keycode);
    }

    private Stage hud;
    private Table content;
    private ButtonGroup areaButtons;
    private TextButton structurebtn,sentryTowerbtn,shadebtn,targetbtn,runbtn;

    private static final float BUTTONWIDTH = 160;

    public void createHud()
    {
        hud = new Stage(new ScalingViewport(Scaling.fit, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        content = new Table();
        content.setFillParent(true);

        content.setDebug(true);

        structurebtn = new TextButton("Structure", StateManager.skin);
        structurebtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                AreaFactory.setAreaType(Area.AreaType.STRUCTURE);
            }
        });
        content.add(structurebtn).width(BUTTONWIDTH);
        content.row();

        sentryTowerbtn = new TextButton("Sentry Tower", StateManager.skin);
        sentryTowerbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                AreaFactory.setAreaType(Area.AreaType.SENTRYTOWER);
            }
        });
        content.add(sentryTowerbtn).width(BUTTONWIDTH);
        content.row();

        shadebtn = new TextButton("Shade", StateManager.skin);
        shadebtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                AreaFactory.setAreaType(Area.AreaType.SHADE);
            }
        });
        content.add(shadebtn).width(BUTTONWIDTH);
        content.row();

        targetbtn = new TextButton("Target", StateManager.skin);
        targetbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                AreaFactory.setAreaType(Area.AreaType.TARGET);
            }
        });
        content.add(targetbtn).width(BUTTONWIDTH);
        content.row();

        runbtn = new TextButton("Run", StateManager.skin);
        runbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentArea();
                stateManager.push(new AISettingsState(stateManager, map));
            }
        });

        content.add(runbtn).expandY().width(BUTTONWIDTH).bottom();
        content.right().pad(20f);

        hud.addActor(content);
    }

    /**
     * If current area is valid, add it to the map and set it to null
     */
    public void saveCurrentArea()
    {
        if(currentArea != null)
        {
            if (areaValid)
                map.addArea(currentArea);
            currentArea = null;
        }
    }

    /**
     * Round the coordinates of a point to the nearest multiple of the grid size
     */
    public void snapToGrid(Vector2 point)
    {
        point.x = Math.round(point.x / gridSize) * gridSize;
        point.y = Math.round(point.y / gridSize) * gridSize;
    }

}
