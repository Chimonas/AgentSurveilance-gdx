package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.worldAttributes.agents.AI;

public abstract class IntruderAI extends AI
{
    public enum IntruderAIType
    {
        STUPID
    }

    public IntruderAI(Intruder intruder)
    {
        agent = intruder;
    }
}
