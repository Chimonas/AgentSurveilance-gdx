package com.mygdx.game.worldAttributes.agents;

import com.mygdx.game.worldAttributes.agents.ai.intruder.IntruderAI;
import com.mygdx.game.worldAttributes.agents.ai.intruder.Stupid;
import com.mygdx.game.gamelogic.Settings;
import com.mygdx.game.gamelogic.World;

public class Intruder extends Agent
{
    private static final float VISIBILITY = 6.5f;
    private boolean sprinting;

    public Intruder(World world, Settings settings)
    {
        super(world, settings);
        visibility = VISIBILITY;
        ai = new Stupid(world.getMap());

        sprinting = false;
    }

    @Override
    public void update()
    {
        if(active)
        {
            ((IntruderAI) ai).update(velocity, angleFacing, getVisibleAgents());
            super.update();
        }
    }

    public void setSprinting(boolean sprinting)
    {
        this.sprinting = sprinting;
    }
}
