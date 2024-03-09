package com.game.tama.util;

public class Bounds
{
    public static boolean isInside(float x, float y, float xstart, float ystart, float width, float height)
    {
        return x > xstart && y > ystart && x < xstart + width && y < ystart + height;
    }
}
