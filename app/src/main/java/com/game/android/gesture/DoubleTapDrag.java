package com.game.android.gesture;

import com.game.tama.core.Input;

public class DoubleTapDrag extends GestureEvent
{
    float prevX;
    float prevY;
    float nextX;
    float nextY;

    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDrag(prevX, prevY, nextX, nextY);
    }

    public void set(float prevX, float prevY, float nextX, float nextY)
    {
        this.prevX = prevX;
        this.prevY = prevY;
        this.nextX = nextX;
        this.nextY = nextY;
    }

    @Override
    public Type type()
    {
        return Type.drag;
    }
}
