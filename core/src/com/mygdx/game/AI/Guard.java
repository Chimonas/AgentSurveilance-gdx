package com.mygdx.game.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Guard extends Agent {

    public static final Guard STUPID = null;
    public Guard(double[] initPos, float initAngle) {
        super(initPos, initAngle);
        body.setCanRun(false);
        body.setVisualRange(0.0f,6.0f);

        System.out.println(Gdx.files.getLocalStoragePath());
        System.out.println();

        body.setTexture(new Texture (Gdx.files.internal("core/assets/").child("red_dot.png")));
    }

    //THERES A TIMER HERE TO BE CHANGED TO TICS
    //input of this function is:
    //0 for entering the Sentry tower
    //1 for leaving the Sentry tower
    public void circulationSentryTower(int movement){
        long startTime = System.nanoTime();
        long elapsedTime = 0L;

        float v = body.getVelocity();
        float a = body.getAudioCapability();

        body.setVisualRange(0.0f, 0.0f);
        body.setVelocity(0.0f);
        body.setAudioCapability(0.0f);

        while (elapsedTime < 3*1000000000) {
            elapsedTime = System.nanoTime() - startTime;
        }

        //Depending on the circulation the visiual range is going to change
        if(movement == 0) {
            body.setVisualRange(2.0f, 15.0f);
            body.setVisualAngle(30.0f);
        }
        else body.setVisualRange(0.0f, 6.0f);
        body.setVelocity(v);
        body.setAudioCapability(a);
        body.setVisualAngle(45.0f);
    }


}
