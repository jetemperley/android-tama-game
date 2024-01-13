package com.tama.core;

import android.graphics.Matrix;

import com.tama.util.Log;
import com.tama.util.MatrixUtil;
import com.tama.util.Vec2;

public abstract class Button
{
    transient Displayable sprite;
    protected String asset = Assets.static_poop;
    Vec2<Float> pos;

    public Button(float x, float y)
    {
        pos = new Vec2<Float>(x, y);
        loadAsset();
    }

    void draw(DisplayAdapter display)
    {
        display.displayAbsolute(sprite, pos.x, pos.y);
    }

    void update()
    {
    }

    void loadAsset()
    {
        sprite = Assets.getSprite(asset);
    }

    abstract void onClick();

    public boolean isInside(float x, float y, Matrix matrix)
    {
        float[] loc = MatrixUtil.convertScreenToMatrix(matrix, x, y);
        return (
            loc[0] > pos.x &&
            loc[1] > pos.y &&
            loc[0] < pos.x + 16 &&
            loc[1] < pos.y + 16);
    }

}
