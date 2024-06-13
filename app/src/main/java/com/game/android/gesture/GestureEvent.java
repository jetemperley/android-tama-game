package com.game.android.gesture;

public abstract class GestureEvent
{

    public float x, y;

    public abstract void callEvent(Input handler);

    public void setTouch(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

}

