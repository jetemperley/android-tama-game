package com.tama.apptest;

public class WorldObject implements java.io.Serializable{

    transient Displayable sprite;
    int x, y;
    int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    boolean flat = false;

    WorldObject(Displayable img) {

        sprite = img;
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }


    void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

}
