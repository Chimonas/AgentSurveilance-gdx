package com.mygdx.game.worldAttributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.Agent;

public class Sound
{
        Color lineColor;
        float x, y, radius;

        public Sound(Agent a){
            x = a.getPosition().x;
            y = a.getPosition().y;
            radius = a.getNoiseGeneratedRadius();
        }


        //For each 25m^2 area there occurs a random noise at a random location
        // to a Poison process of 0.1 ocurence per minute which you can hear at a 5m distance
        public Sound(Map map) {

            x = (float) Math.random() * map.getWidth();
            y = (float) Math.random() * map.getHeight();
            radius = 5;
        };

        public void renderGeneratedNoise(ShapeRenderer shapeRenderer)
        {

        }


        //TODO: AUDIO IMPLEMENTATION

        //AUDIO
        //Hearing = sound coming from its position with normal distributed uncertainty
        //with a std dev of 10 degrees
        public Vector2 checkNoiseAround(Vector2 pos){

            return null;
        }

}
