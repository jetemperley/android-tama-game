package com.tama.gesture;

import com.tama.core.InputHandler;
import com.tama.util.Vec2;

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
    public void callEvent(InputHandler handler)
    {
        handler.scale(prev1, prev2, next1, next2);
    }
}
