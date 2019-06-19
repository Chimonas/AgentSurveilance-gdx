package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAIFactory;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class Intruder extends Agent
{
    public static final float VISIBILITY = 6.5f;
//    private boolean sprinting;

    public Intruder(World world)
    {
        super(world);
        visibility = VISIBILITY;
//        sprinting = false;
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


//    public void setSprinting(boolean sprinting)
//    {
//        this.sprinting = sprinting;
//    }
}
