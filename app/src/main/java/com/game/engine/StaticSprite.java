package com.game.engine;

public class StaticSprite implements Sprite
{

    int imgId;

    public StaticSprite(final int imgId)
    {
        this.imgId = imgId;
    }

    @Override
    public int getSpriteId()
    {
        return imgId;
    }

    @Override
    public int getUISpriteId()
    {
        return imgId;
    }
}
