package com.tama.util;

import com.tama.core.InputHandler;

public abstract class GestureEvent
{
    float x, y;

    public GestureEvent(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract void callEvent(InputHandler handler);
}
