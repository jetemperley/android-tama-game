package com.game.engine.gesture.gestureEvent;

import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class Drag extends GestureEvent
{
    public final Vec2<Float> prev = new Vec2<>(0f, 0f);
    public final Vec2<Float> next = new Vec2<>(0f, 0f);

    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.drag(prev, next);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.drag(this);
    }

    public void set(final Vec2<Float> prev, final Vec2<Float> next)
    {
        this.prev.set(prev);
        this.next.set(next);
    }

    @Override
    public GestureEvent transform(final Transform transform)
    {
        final Drag copy = (Drag) super.transform(transform);

        final Vec4<Float> pt = transform.mapVector(prev.x, prev.y, 0);
        copy.prev.set(pt.x, pt.y);

        final Vec4<Float> nt = transform.mapVector(next.x, next.y, 0);
        copy.next.set(nt.x, nt.y);

        return copy;
    }

    @Override
    public GestureEvent copy()
    {
        final Drag copy = (Drag) super.copy();
        copy.set(this.prev, this.next);
        return copy;
    }

    @Override
    public EventType getType()
    {
        return EventType.DRAG;
    }
}
