package com.mygdx.game.agents;

import com.mygdx.game.agents.ai.guard.Stupid;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.gamelogic.Settings;

public class Guard extends Agent
{
//    public static final Guard STUPID = null;

    public Guard(Map map, Settings settings)
    {
        super(map, settings, 7.5f);
        ai = new Stupid();
    }

//    //THERES A TIMER HERE TO BE CHANGED TO TICS
//    //input of this function is:
//    //0 for entering the Sentry tower
//    //1 for leaving the Sentry tower
//    public void circulationSentryTower(int movement){
//        long startTime = System.nanoTime();
//        long elapsedTime = 0L;
//
//        float v = body.getVelocity();
//        float a = body.getAudioCapability();
//
//        body.setVisualRange(0.0f, 0.0f);
//        body.setVelocity(0.0f);
//        body.setAudioCapability(0.0f);
//
//        while (elapsedTime < 3*1000000000) {
//            elapsedTime = System.nanoTime() - startTime;
//        }
//
//        //Depending on the circulation the visiual range is going to change
//        if(movement == 0) {
//            body.setVisualRange(2.0f, 15.0f);
//            body.setVisualAngle(30.0f);
//        }
//        else body.setVisualRange(0.0f, 6.0f);
//        body.setVelocity(v);
//        body.setAudioCapability(a);
//        body.setVisualAngle(45.0f);
//    }
}
