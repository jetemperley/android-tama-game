package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class Drag extends GestureEvent
{
    Vec2<Float> prev = new Vec2<Float>(0f, 0f);
    Vec2<Float> next = new Vec2<Float>(0f, 0f);

    @Override
    public void callEvent(Input handler)
    {
        handler.drag(prev, next);
    }

    public void setPrevNext(Vec2<Float> prev, Vec2<Float> next)
    {
        this.prev = prev;
        this.next = next;
    }

    @Override
    public GestureEvent transform(Transform transform) {
        Drag copy = (Drag) super.transform(transform);
        float[] pt = transform.mapVector(prev.x, prev.y, 0);
        copy.prev.x = pt[0];
        copy.prev.y = pt[1];
        float[] nt = transform.mapVector(next.x, next.y, 0);
        copy.next.x = nt[0];
        copy.next.y = nt[1];
        return copy;
    }
}
