package com.game.android.gesture;

import com.game.tama.core.Input;

public abstract class GestureEvent
{
    public enum Type
    {
        press,
        drag,
        dragStart,
        dragEnd,
    }

    public float x, y;

    public abstract void callEvent(Input handler);

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    abstract public Type type();
}

