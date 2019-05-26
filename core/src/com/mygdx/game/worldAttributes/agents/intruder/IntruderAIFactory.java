package com.mygdx.game.worldAttributes.agents.intruder;

public class IntruderAIFactory
{
    public static IntruderAI newIntruderAI(IntruderAI.IntruderAIType intruderAIType, Intruder intruder)
    {
        switch (intruderAIType)
        {
            case STUPID:
                return new Stupid(intruder);
            default:
                return new Stupid(intruder);

            //TODO: catch aitype not supported
        }
    }
}
