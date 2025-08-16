package com.game.engine.gesture.gestureEvent;

import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class DoubleTapDrag extends GestureEvent
{
    public Vec2<Float> prev = new Vec2<>(0f, 0f);
    public Vec2<Float> next = new Vec2<>(0f, 0f);

    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.doubleTapDrag(prev.x, prev.y, next.x, next.y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.doubleTapDrag(this);
    }

    public void set(final Vec2<Float> prev, final Vec2<Float> next)
    {
        this.prev.set(prev);
        this.next.set(next);
    }

    public void set(final float prevX, final float prevY, final float nextX, final float nextY)
    {
        this.prev.set(prevX, prevY);
        this.next.set(nextX, nextY);
    }

    @Override
    public GestureEvent transform(final Transform transform)
    {
        final DoubleTapDrag copy = (DoubleTapDrag) super.transform(transform);
        final Vec4<Float> prev = transform.mapVector(this.prev.x, this.prev.y, 0);
        copy.prev.x = prev.x;
        copy.prev.y = prev.y;
        final Vec4<Float> next = transform.mapVector(this.next.x, this.next.y, 0);
        copy.next.x = next.x;
        copy.next.y = next.y;
        return copy;
    }

    @Override
    public GestureEvent copy()
    {
        final DoubleTapDrag copy = (DoubleTapDrag) super.copy();
        copy.next.set(this.next);
        copy.prev.set(this.prev);
        return copy;
    }

    @Override
    public EventType getType()
    {
        return EventType.DOUBLE_TAP_DRAG;
    }
}