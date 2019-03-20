package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends Area {


    public Wall(int x, int y, int width, int height) {
        super(x,y,width,height);
        areaTexture = new Texture("core/assets/areas/Wall.png");
    }


}
