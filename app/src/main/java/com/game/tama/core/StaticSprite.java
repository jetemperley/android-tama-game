package com.game.tama.core;

public class StaticSprite implements Sprite
{

    int imgId;

    public StaticSprite(final int imgId)
    {
        this.imgId = imgId;
    }

    public int getSpriteId()
    {
        return imgId;
    }

    public int getUISpriteId()
    {
        return imgId;
    }
}
