package com.mygdx.game.agents;

import com.mygdx.game.agents.ai.guard.Stupid;
import com.mygdx.game.gamelogic.Settings;

public class Guard extends Agent
{
//    public static final Guard STUPID = null;

    public Guard(Settings settings)
    {
        super(settings, 7.5f);
        ai = new Stupid();
    }

//    //input of this function is:
//    //0 for entering the Sentry tower
//    //1 for leaving the Sentry tower
//    public void enterLeaveSentryTower(int movement){
//        long startTime = System.nanoTime();
//        long elapsedTime = 0L;
//
//        float v = getVelocity();
//        float a = getAudioCapability();
//
//        setVisualRange(0.0f, 0.0f);
//        setVelocity(0.0f);
//        setAudioCapability(0.0f);
//
//        while (elapsedTime < 3*1000000000) {
//            elapsedTime = System.nanoTime() - startTime;
//        }
//
//        //Depending on the circulation the visiual range is going to change
//        if(movement == 0) {
//            setVisualRange(2.0f, 15.0f);
//            setVisualAngle(30.0f);
//        }
//        else {
//            setVisualRange(0.0f, 6.0f);
//            setVisualAngle(45.0f);
//        }
//        setVelocity(v);
//        setAudioCapability(a);
//    }
}
