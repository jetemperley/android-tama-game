package com.tama.core;

import com.tama.util.Vec2;

public class WorldObject implements java.io.Serializable
{

    public transient Displayable sprite;
    protected String asset;
    public int x, y;
    public int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    public boolean flat = false;

    public WorldObject(Displayable img)
    {
        sprite = img;
        x = 0;
        y = 0;
        xoff = 0;
        yoff = 0;
    }


    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2<Float> getWorldPos()
    {
        return new Vec2<Float>(x + xoff / 100f, y + yoff / 100f);
    }


}
