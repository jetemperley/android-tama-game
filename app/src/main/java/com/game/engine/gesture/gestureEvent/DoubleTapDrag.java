package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
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
    public GestureEvent transform(Transform transform) {
        DoubleTapDrag copy = (DoubleTapDrag) super.transform (transform);
        float[] prev = transform.mapVector(prevX, prevY, 0);
        copy.prevX = prev[0];
        copy.prevY = prev[1];
        float[] next = transform.mapVector(nextX, nextY, 0);
        copy.nextX = next[0];
        copy.nextY = next[1];
        return copy;
    }
}
