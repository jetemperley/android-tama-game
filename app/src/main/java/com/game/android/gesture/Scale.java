package com.game.android.gesture;

import com.game.engine.Transform;
import com.game.tama.util.Vec2;

public class Scale extends GestureEvent
{
    Vec2<Float> prev1;
    Vec2<Float> prev2;
    Vec2<Float> next1;
    Vec2<Float> next2;

    public Scale()
    {
        prev1 = new Vec2<Float>(0f, 0f);
        prev2 = new Vec2<Float>(0f, 0f);
        next1 = new Vec2<Float>(0f, 0f);
        next2 = new Vec2<Float>(0f, 0f);
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
        this.prev1 = prev1;
        this.prev2 = prev2;
        this.next1 = next1;
        this.next2 = next2;
    }

    @Override
    public void transform(Transform transform) {
        super.transform(transform);
        transform(transform, prev1);
        transform(transform, prev2);
        transform(transform, next1);
        transform(transform, next2);
    }

    private static void transform(Transform transform, Vec2<Float> vec) {
        float[] temp = transform.mapVector(vec.x, vec.y, 0);
        vec.x = temp[0];
        vec.y = temp[1];
    }
}
