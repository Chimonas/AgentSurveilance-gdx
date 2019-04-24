package com.mygdx.game.states.visualStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.StateManager;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.states.State;

public abstract class VisualState extends State
{
    protected ShapeRenderer shapeRenderer;
    protected OrthographicCamera camera;
    protected Map map;

    protected VisualState(StateManager stateManager, Map map)
    {
        super(stateManager);
        this.map = map;
    }

    protected float aspectRatio;
    protected final float   ZOOMMIN = .1f,
                            ZOOMMAX = 2.0f,
                            ZOOMSTEP = 1.1f,
                            MOVESTEP = 10.0f;
    protected float effectiveMoveStep, effectiveViewportWidth, effectiveViewportHeight;

    protected boolean dragging;

    /**
     * Sets up ShapeRenderer and OrthographicCamera
     */
    @Override
    public void create()
    {
        Gdx.input.setInputProcessor(this);

        shapeRenderer = new ShapeRenderer();

        aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        if(map.getWidth()/map.getHeight() < aspectRatio)
            camera = new OrthographicCamera(map.getHeight() * aspectRatio, map.getHeight());
        else
            camera = new OrthographicCamera(map.getWidth(),map.getWidth() / aspectRatio);

        camera.position.set(map.getWidth() / 2, map.getHeight() / 2, 0);
        camera.zoom = ZOOMSTEP;

        effectiveMoveStep = camera.zoom * MOVESTEP;
        effectiveViewportWidth = camera.zoom * camera.viewportWidth;
        effectiveViewportHeight = camera.zoom * camera.viewportHeight;

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        dragging = false;
    }

    /**
     * Paints the background and renders the map
     */
    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0f,0f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void dispose()
    {
        shapeRenderer.dispose();
    }

    /**
     * Start the dragging of the map
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.RIGHT)
            dragging = true;

        return super.touchDown(screenX, screenY,pointer,button);
    }

    /**
     * Updates the position of the map if dragging
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float deltaX = Gdx.input.getDeltaX();
        float deltaY = Gdx.input.getDeltaY();

        if (dragging)
        {
            camera.translate(scaleScreenInput(-deltaX, deltaY));

            confineCamera(new Vector2(0,0), new Vector2(map.getWidth(), map.getHeight()));
        }

        return super.touchDragged(screenX,screenY,pointer);
    }

    /**
     * Stops the dragging of the map
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.RIGHT)
            dragging = false;

        return super.touchUp(screenX,screenY,pointer,button);
    }

    /**
     * Moves camera around with arrow keys, keeps it within bounds of the world
     */
    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.ESCAPE)
        {
            stateManager.home();
        }

        if(keycode == Input.Keys.UP)
            camera.translate(0f, effectiveMoveStep);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0f, -effectiveMoveStep);
        if(keycode == Input.Keys.LEFT)
            camera.translate(-effectiveMoveStep, 0f);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(effectiveMoveStep, 0f);

        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT)
            confineCamera(new Vector2(0,0), new Vector2(map.getWidth(), map.getHeight()));

        return super.keyDown(keycode);
    }

    /**
     * When scrolling calls betterZoom with zoomAmount based on scroll direction
     * @param amount +1 is zoom out, -1 is zoom in
     */
    @Override
    public boolean scrolled(int amount)
    {
        if(amount > 0)
        {
            if (camera.zoom * ZOOMSTEP <= ZOOMMAX)
                betterZoom(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), ZOOMSTEP);
        }
        else
        {
            if (camera.zoom / ZOOMSTEP >= ZOOMMIN)
                betterZoom(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1 / ZOOMSTEP);
        }

        return super.scrolled(amount);
    }

    /**
     * Zooms the camera while keeping an (x,y) coordinate at the same screen position
     * @param screenX x coordinate
     * @param screenY y coordinate
     * @param zoomAmount how much to zoom by
     */
    public void betterZoom(float screenX, float screenY, float zoomAmount)
    {
        float zoomDifference = camera.zoom * (zoomAmount - 1);

        camera.position.x += camera.viewportWidth * zoomDifference * ( 0.5 - screenX / Gdx.graphics.getWidth());
        camera.position.y += camera.viewportHeight * zoomDifference * ( 0.5 - screenY / Gdx.graphics.getHeight());

        confineCamera(new Vector2(0,0), new Vector2(map.getWidth(), map.getHeight()));

        camera.zoom *= zoomAmount;

        effectiveMoveStep = camera.zoom * MOVESTEP;
        effectiveViewportWidth = camera.zoom * camera.viewportWidth;
        effectiveViewportHeight = camera.zoom * camera.viewportHeight;
    }

    /**
     * Takes coordinates on the screen and transforms them to the corresponding coordinates in the world
     */
    public Vector2 transformScreenInput(float screenX, float screenY)
    {
        Vector2 worldCoordinates = new Vector2();

        Vector2 scaledScreenInput = scaleScreenInput(screenX,screenY);

        worldCoordinates.x = camera.position.x - (effectiveViewportWidth / 2) + scaledScreenInput.x;
        worldCoordinates.y = camera.position.y - (effectiveViewportHeight / 2) + scaledScreenInput.y;

        return worldCoordinates;
    }

    public Vector2 scaleScreenInput(float screenX, float screenY)
    {
        Vector2 scaledCoordinates = new Vector2();

        scaledCoordinates.x = screenX * effectiveViewportWidth / Gdx.graphics.getWidth();
        scaledCoordinates.y = screenY * effectiveViewportHeight / Gdx.graphics.getHeight();

        return scaledCoordinates;
    }

    public void confineCamera(Vector2 startPosition, Vector2 endPosition)
    {
        if(camera.position.x < startPosition.x){camera.position.x = startPosition.x;}
        else if (camera.position.x > endPosition.x){camera.position.x = endPosition.x;}
        if(camera.position.y < startPosition.y){camera.position.y = startPosition.y;}
        else if (camera.position.y > endPosition.y){camera.position.y = endPosition.y;}
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }
}
