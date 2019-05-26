package com.mygdx.game.worldAttributes;

import com.mygdx.game.worldAttributes.agents.Agent;

import java.util.ArrayList;

public class Communication
{
    private Agent sendingAgent, receivingAgent;
    private ArrayList<?> message;

    public Communication(Agent sendingAgent, Agent receivingAgent, ArrayList<?> message)
    {
        this.sendingAgent = sendingAgent;
        this.receivingAgent = receivingAgent;
        this.message = message;
    }

    public Agent getSendingAgent()
    {
        return sendingAgent;
    }

    public Agent getReceivingAgent()
    {
        return receivingAgent;
    }

    public ArrayList<?> getMessage()
    {
        return message;
    }
}
