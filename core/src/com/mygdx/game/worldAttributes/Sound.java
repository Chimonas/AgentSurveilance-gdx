package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.agents.Agent;

import java.util.ArrayList;

public class Sound
{
    private Vector2 position;
    private float visibility;

    private ArrayList<Agent> agentsReceived = new ArrayList<>();

    public Sound(Vector2 position, float visibility)
    {
        this.position = position;
        this.visibility = visibility;
    }

    public Sound(Vector2 position, float visibility,Agent agent)
    {
        this.position = position;
        this.visibility = visibility;
        this.agentsReceived.add(agent);
    }

    public boolean soundReceivable(Agent a) {
        for (Agent agent : agentsReceived) {
            if (agent == a) {
                return false;
            }
        }
        agentsReceived.add(a);
        return true;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public float getVisibility()
    {
        return visibility;
    }
}
