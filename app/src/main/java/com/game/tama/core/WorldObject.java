package com.game.tama.core;

import com.game.tama.util.Vec2;

public class WorldObject implements java.io.Serializable
{

    public transient Sprite sprite;
    protected String asset;
    /** Position of this thing in the array */
    public int x, y;
    /** Offsets in percentage of 1 array square */
    public int xoff, yoff;
    // flat indicates if the sprite is displayed as a ground base layer
    public boolean flat = false;

    public WorldObject(Sprite img)
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

    /**
     * Gets the array location of the object
     * @return
     */
    public Vec2<Float> getWorldArrPos()
    {
        return new Vec2<Float>(x + xoff / 100f, y + yoff / 100f);
    }

    public Vec2<Float> getWorldBitPos()
    {
        return new Vec2<Float>(x*16 + xoff/100f*16, y*16 + yoff/100f*16);
    }

    public float[] getWorldBitPosAsArray()
    {
        return new float[] {x*16 + xoff/100f*16, y*16 + yoff/100f*16};
    }

    public void setOffset(int xoff, int yoff)
    {
        this.xoff = xoff;
        this.yoff = yoff;
    }

    public String posString()
    {
        return x + " " + y;
    }
}
