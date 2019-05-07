package com.mygdx.game.worldAttributes.agents.intruder;

public class Stupid extends IntruderAI
{
    public Stupid(Intruder intruder)
    {
        super(intruder);
    }

    @Override
    public float getNewAngle()
    {
        return intruder.getAngle() + 2.0f * ((float)Math.random() - 0.5f) * 30.0f;
    }

    @Override
    public float getNewVelocity()
    {
        return intruder.getVelocity() + 2.0f * ((float)Math.random() - 0.5f) * 1.0f;
    }
}
