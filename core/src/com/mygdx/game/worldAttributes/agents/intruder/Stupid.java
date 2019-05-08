package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.gamelogic.GameLoop;

public class Stupid extends IntruderAI
{
    public Stupid(Intruder intruder)
    {
        super(intruder);
    }

    public float getNewAngle()
    {
        return intruder.getAngle() + 2.0f * ((float)Math.random() - 0.5f) * 300.0f / (float)GameLoop.TICKRATE;
    }

    public float getNewVelocity()
    {
        return intruder.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 20.0f / (float)GameLoop.TICKRATE;
    }
}
