package com.mygdx.game.agents;

import com.mygdx.game.agents.ai.intruder.Stupid;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

public class Intruder extends Agent
{
    private boolean canRun;

    public Intruder(Settings settings, Map map)
    {
        super(settings, 6.5f);
        ai = new Stupid(map);

        canRun = true;
    }

    public void sprint() {

        float t = System.currentTimeMillis();

        //If one decides to sprint it'll be for 5 seconds straight
        while(canRun){
            setVelocity(3.0f);
            setTurnAngle(10.0f);

            if((System.currentTimeMillis() - t)/1000 > 5){
                canRun = false;
                setVelocity(1.4f);
                setTurnAngle(180.0f);
                setRestTime(System.currentTimeMillis());
            }
        }
    }

    private void setRestTime(long timeMillisEndSprint) {

        //One has to rest after a sprint for at least 10 seconds
        while((System.currentTimeMillis() - timeMillisEndSprint)/1000 > 10) continue;
        canRun = true;
    }
}
