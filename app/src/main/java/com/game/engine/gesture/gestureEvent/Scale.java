package com.game.engine.gesture.gestureEvent;

import com.game.engine.Transform;
import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;
import com.game.tama.util.Vec2;
import com.game.tama.util.Vec4;

public class Scale extends GestureEvent
{
    final public Vec2<Float> prev1;
    final public Vec2<Float> prev2;
    final public Vec2<Float> next1;
    final public Vec2<Float> next2;

    public Scale()
    {
        prev1 = new Vec2<>(0f, 0f);
        prev2 = new Vec2<>(0f, 0f);
        next1 = new Vec2<>(0f, 0f);
        next2 = new Vec2<>(0f, 0f);
    }

    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.scale(prev1, prev2, next1, next2);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.scale(this);
    }

    public void set(final Vec2<Float> prev1,
                    final Vec2<Float> prev2,
                    final Vec2<Float> next1,
                    final Vec2<Float> next2)
    {
        this.prev1.set(prev1);
        this.prev2.set(prev2);
        this.next1.set(next1);
        this.next2.set(next2);
    }

    @Override
    public GestureEvent transform(final Transform transform)
    {
        final Scale copy = (Scale) super.transform(transform);
        transform(transform, copy.prev1);
        transform(transform, copy.prev2);
        transform(transform, copy.next1);
        transform(transform, copy.next2);
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
        final Scale copy = (Scale) super.copy();
        copy.set(prev1, prev2, next1, next2);
        return copy;
    }

    @Override
    public EventType getType()
    {
        return EventType.SCALE;
    }
}
