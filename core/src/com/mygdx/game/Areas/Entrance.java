package com.mygdx.game.areas;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.agents.Agent;
import com.mygdx.game.agents.Guard;

public class Entrance
{
    private EntranceType type;
    private Vector2 startPosition, endPosition;

    private enum EntranceType
    {
        DOOR(5.0f, 5.0f),
        WINDOW(10.0f, 3.0f);

        private float soundDistance, enteringTime;

        EntranceType(float soundDistance, float enteringTime)
        {
            this.soundDistance = soundDistance;
            this.enteringTime = enteringTime;
        }
    }

    public Entrance(EntranceType type, Vector2 startPosition, Vector2 endPosition)
    {
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

//    public void enterWithoutSound()
//    {
//        if(type == "door")
//        {
//            enteringSoundDistance = 0.0;
//            Random random = new Random();
//            enteringTime = random.nextGaussian()*2 + 12.0; //Normal distribution with std 2 and mean = 12
//        }
//    }
//
//    public void changePosition(double[] begP, double[] endP){
//        begPos = begP;
//        endPos = endP;
//    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }

    //to check if can enter the Sentry tower
    public boolean canEnterSentryTower(Agent agent) {
        if (agent instanceof Guard) return true;
        return false;
    }
}

