package com.tama.apptest;

public class WorldObject {

    Displayable sprite;
    int x, y;
    float xoff, yoff;

    WorldObject(){
        this(null);
    }
    WorldObject(Displayable img) {

        sprite = img;
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }

    void display(DisplayAdapter d){
        d.displayWorld(this);
    }
}
