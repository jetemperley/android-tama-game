package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class DoubleTapDrag extends GestureEvent
{
    Vec2<Float> prev = new Vec2<>(0f, 0f);
    Vec2<Float> next = new Vec2<>(0f, 0f);

    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDrag(prev.x, prev.y, next.x, next.y);
    }

    public void set(Vec2<Float> prev, Vec2<Float> next)
    {
        this.prev.set(prev);
        this.next.set(next);
    }

    public void set(float prevX, float prevY, float nextX, float nextY)
    {
        this.prev.set(prevX, prevY);
        this.next.set(nextX, nextY);
    }

    @Override
    public GestureEvent transform(Transform transform) {
        DoubleTapDrag copy = (DoubleTapDrag) super.transform (transform);
        float[] prev = transform.mapVector(this.prev.x, this.prev.y, 0);
        copy.prev.x = prev[0];
        copy.prev.y = prev[1];
        float[] next = transform.mapVector(this.next.x, this.next.y, 0);
        copy.next.x = next[0];
        copy.next.y = next[1];
        return copy;
    }

    @Override
    public GestureEvent copy()
    {
        DoubleTapDrag copy = (DoubleTapDrag) super.copy();
        copy.next.set(this.next);
        copy.prev.set(this.prev);
        return copy;
    }
}
