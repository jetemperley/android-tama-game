package com.game.engine.gesture.gestureEvent;

import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class ScaleRelease extends GestureEvent
{
    final public Vec2<Float> point1;
    final public Vec2<Float> point2;

    public ScaleRelease()
    {
        point1 = new Vec2<>(0f, 0f);
        point2 = new Vec2<>(0f, 0f);
    }

    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.scaleRelease(point1, point2);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.scaleRelease(this);
    }

    public void set(final Vec2<Float> point1,
                    final Vec2<Float> point2)
    {
        this.point1.set(point1);
        this.point2.set(point2);
    }

    @Override
    public GestureEvent transform(final Transform transform)
    {
        final ScaleRelease copy = (ScaleRelease) super.transform(transform);
        transform(transform, copy.point1);
        transform(transform, copy.point2);
        return copy;
    }

    private static void transform(final Transform transform, final Vec2<Float> vec)
    {
        final Vec4<Float> temp = transform.mapVector(vec.x, vec.y, 0);
        vec.x = temp.x;
        vec.y = temp.y;
    }

    @Override
    public GestureEvent copy()
    {
        final ScaleRelease copy = (ScaleRelease) super.copy();
        copy.set(point1, point2);
        return copy;
    }

    @Override
    public EventType getType()
    {
        return EventType.SCALE_RELEASE;
    }
}
