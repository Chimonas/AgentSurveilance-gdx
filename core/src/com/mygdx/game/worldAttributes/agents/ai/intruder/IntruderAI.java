package com.mygdx.game.worldAttributes.agents.ai.intruder;

import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.ai.AI;

import java.util.ArrayList;

public interface IntruderAI extends AI
{
    enum AIType
    {
        STUPID
    }

    void update(float velocity, float angle, ArrayList<Agent> agents);
}
