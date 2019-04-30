package com.mygdx.game.agents;

import com.mygdx.game.agents.ai.intruder.Stupid;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

public class Intruder extends Agent
{
    private boolean sprinting;

    public Intruder(Map map, Settings settings)
    {
        super(map, settings, 6.5f);
        ai = new Stupid(map);

        sprinting = false;
    }

    public void setSprinting(boolean sprinting)
    {
        this.sprinting = sprinting;
    }
}
