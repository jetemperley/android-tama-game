package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class Drag extends GestureEvent
{
    public final Vec2<Float> prev = new Vec2<>(0f, 0f);
    public final Vec2<Float> next = new Vec2<>(0f, 0f);

    @Override
    public void callEvent(Input handler)
    {
        handler.drag(prev, next);
    }

    public void set(Vec2<Float> prev, Vec2<Float> next)
    {
        this.prev.set(prev);
        this.next.set(next);
    }

    @Override
    public GestureEvent transform(Transform transform) {
        Drag copy = (Drag) super.transform(transform);
        float[] pt = transform.mapVector(prev.x, prev.y, 0);
        copy.prev.set(pt[0], pt[1]);
        float[] nt = transform.mapVector(next.x, next.y, 0);
        copy.next.set(nt[0], nt[1]);
        return copy;
    }

    @Override
    public GestureEvent copy()
    {
        Drag copy = (Drag) super.copy();
        copy.set(this.prev, this.next);
        return copy;
    }
}
