package com.tama.gesture;

import com.tama.core.Interactive;

public abstract class GestureEvent
{
    public float x, y;

    public abstract void callEvent(Interactive handler);

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}

