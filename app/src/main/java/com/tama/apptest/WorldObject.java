package com.tama.apptest;

public class WorldObject implements java.io.Serializable{

    Displayable sprite;
    private int x, y;
    int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    boolean flat = true;

    WorldObject(Displayable img) {

        sprite = img;
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }

    void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    int x(){
        return x;
    }
    int y(){
        return y;
    }
    void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

}
