package com.mygdx.game.AI;
public class Intruder extends Agent {

    public static final Intruder STUPID = null;

    public Intruder(double[] initPos, float initAngle) {
        super(initPos, initAngle);
        body.setCanRun(true);
        body.setVisualRange(0.0f,7.5f);
    }



}
