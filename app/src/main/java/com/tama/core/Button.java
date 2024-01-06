package com.tama.core;

import android.graphics.Matrix;

import com.tama.util.Log;
import com.tama.util.MatrixUtil;
import com.tama.util.Vec2;

public abstract class Button
{
    transient Displayable sprite;
    private String asset = Assets.static_poop;
    Vec2<Float> pos;
    private Matrix matrix;

    public Button(float x, float y, Matrix mat)
    {
        matrix = mat;
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

    void onClick()
    {

    }

//    public boolean isInside(float x, float y)
//    {
//        y -= GameActivity.TOP_OFFSET;
//        x = x/4;
//        y = y/4;
//        Log.log(this, "raw tap " + x + " " + y);
//        return (x > pos.x && y > pos.y && x < pos.x + 16 && y < pos.y + 16);
//    }

    public boolean isInside(float x, float y)
    {
        float[] loc = MatrixUtil.convertScreenToMatrix(matrix, x, y);
        return (
            loc[0] > pos.x &&
            loc[1] > pos.y &&
            loc[0] < pos.x + 16 &&
            loc[1] < pos.y + 16);
    }

}
