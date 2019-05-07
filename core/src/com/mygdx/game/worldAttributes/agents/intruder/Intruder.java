package com.mygdx.game.worldAttributes.agents.intruder;

import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;

public class Intruder extends Agent
{
    private static final float VISIBILITY = 6.5f;
    private boolean sprinting;

    public Intruder(World world, Settings settings)
    {
        super(world, settings);
        visibility = VISIBILITY;
        ai = new Stupid(this);

        sprinting = false;
    }

    @Override
    public void update()
    {
        if(active)
        {
            ai.update();
            super.update();
        }
    }

    public void setSprinting(boolean sprinting)
    {
        this.sprinting = sprinting;
    }
}
