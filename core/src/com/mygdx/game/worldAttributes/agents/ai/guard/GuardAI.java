package com.mygdx.game.worldAttributes.agents.ai.guard;

import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.ai.AI;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public interface GuardAI extends AI
{
    enum AIType
    {
        STUPID
    }

    void update(float velocity, float angle, ArrayList<Area> areas, ArrayList<Agent> agents);
}
