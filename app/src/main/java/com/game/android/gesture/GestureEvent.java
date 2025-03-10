package com.game.android.gesture;

import com.game.engine.Transform;

public abstract class GestureEvent
{

    public float x, y;

    public abstract void callEvent(Input handler);

    public void setTouch(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void transform(Transform transform)
    {
        float[] f = transform.mapVector(x, y, 0);
        x = f[0];
        y = f[1];
    }

}

