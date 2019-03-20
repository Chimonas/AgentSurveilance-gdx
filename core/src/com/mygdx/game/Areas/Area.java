package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class Area extends Rectangle {

    public Texture areaTexture;


    public Area(int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    public Texture getTexture() {
        return areaTexture;
    }
}
