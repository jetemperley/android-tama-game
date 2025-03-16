package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;
import com.game.engine.Transform;
import com.game.tama.util.Vec2;

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
    public void callEvent(Input handler)
    {
        handler.scale(prev1, prev2, next1, next2);
    }

    public void set(Vec2<Float> prev1,
                    Vec2<Float> prev2,
                    Vec2<Float> next1,
                    Vec2<Float> next2)
    {
        this.prev1.set(prev1);
        this.prev2.set(prev2);
        this.next1.set(next1);
        this.next2.set(next2);
    }

    @Override
    public GestureEvent transform(Transform transform) {
        Scale copy = (Scale) super.transform(transform);
        transform(transform, copy.prev1);
        transform(transform, copy.prev2);
        transform(transform, copy.next1);
        transform(transform, copy.next2);
        return copy;
    }

    private static void transform(Transform transform, Vec2<Float> vec) {
        float[] temp = transform.mapVector(vec.x, vec.y, 0);
        vec.x = temp[0];
        vec.y = temp[1];
    }

    @Override
    public GestureEvent copy()
    {
        Scale copy = (Scale) super.copy();
        copy.set(prev1, prev2, next1, next2);
        return copy;
    }
}
