package com.mygdx.game.agents;

import com.mygdx.game.agents.ai.intruder.Stupid;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

public class Intruder extends Agent
{
    private boolean sprinting;

    public Intruder(Settings settings, Map map)
    {
        super(settings, 6.5f);
        ai = new Stupid(map);

        sprinting = false;
    }

    public void setSprinting(boolean sprinting)
    {
        this.sprinting = sprinting;
    }
}
