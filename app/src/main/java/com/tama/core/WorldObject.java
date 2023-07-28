package com.tama.core;

public class WorldObject implements java.io.Serializable
{

    transient Displayable sprite;
    String asset;
    public int x, y;
    public int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    boolean flat = false;

    WorldObject(Displayable img)
    {

        sprite = img;
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }


    void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    void loadAsset()
    {

    }

}
