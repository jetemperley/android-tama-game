package com.game.android.gesture;

import com.game.engine.Transform;

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
    public void transform(Transform transform) {
        super.transform(transform);
        float[] prev = transform.mapVector(prevX, prevY, 0);
        prevX = prev[0];
        prevY = prev[1];
        float[] next = transform.mapVector(nextX, nextY, 0);
        nextX = next[0];
        nextY = next[1];
    }
}
