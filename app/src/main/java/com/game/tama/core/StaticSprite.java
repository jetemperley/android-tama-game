package com.game.tama.core;

import android.graphics.Bitmap;

public class StaticSprite implements Displayable
{

    Bitmap img;

    StaticSprite(Bitmap bm)
    {
        img = bm;
    }

    public Bitmap getSprite()
    {
        return img;
    }

    public Bitmap getUISprite()
    {
        return img;
    }
}
