package com.mygdx.game.worldAttributes.agents.intruder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.GameLoop;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAIFactory;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Intruder extends Agent
{
    public static final float VISIBILITY = 6.5f, SPRINTING_VELOCITY = 3.0f, MAX_SPRINTING_TIME = 5.0f, SPRINTING_COOL_DOWN = 10.0f;
    private boolean sprinting;

    public Intruder(World world)
    {
        super(world);
        visibility = VISIBILITY;

        sprinting = false;
    }

    public void setIntruderAI(IntruderAI.IntruderAIType intruderAIType)
    {
        ai = IntruderAIFactory.newIntruderAI(intruderAIType, this);
    }

    public void setSimulationAI(IntruderAI.IntruderAIType intruderAIType)
    {
        ArrayList<Area> internalAreas = new ArrayList<>();

//        if(ai instanceof ExplorationAI)
//           internalAreas = ((ExplorationAI) ai).getInternalAreas();

        ai = IntruderAIFactory.newIntruderAI(intruderAIType, this, internalAreas);
//        ai = new HeuristicBot(this);
    }

    @Override
    public void update()
    {
        if(sprinting && world.getGameLoop().getTicks() - startSprintingTick >= (int) (MAX_SPRINTING_TIME * GameLoop.TICK_RATE))
            setSprinting(false);
        super.update();
    }

    private int startSprintingTick = (int)(-MAX_SPRINTING_TIME * GameLoop.TICK_RATE), endSprintingTick = (int)(-SPRINTING_COOL_DOWN * GameLoop.TICK_RATE);

    public void setSprinting(boolean sprinting)
    {
        if(sprinting)
        {
            if(world.getGameLoop().getTicks() - endSprintingTick >= (int) (SPRINTING_COOL_DOWN * GameLoop.TICK_RATE))
            {
                startSprintingTick = world.getGameLoop().getTicks();
                maxVelocity = SPRINTING_VELOCITY;
                this.sprinting = true;
            }
        }
        else
        {
            endSprintingTick = world.getGameLoop().getTicks();
            maxVelocity = MAX_VELOCITY;
            this.sprinting = false;
        }
    }
}
