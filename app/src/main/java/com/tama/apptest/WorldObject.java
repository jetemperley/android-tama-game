package com.tama.apptest;

public class WorldObject implements java.io.Serializable{

    // grid location
    int x, y;
    // percentage offsets
    int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    boolean flat = false;

    WorldObject() {

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
