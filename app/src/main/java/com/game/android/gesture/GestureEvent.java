package com.game.android.gesture;

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

    public void setTouch(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    abstract public Type type();
}

