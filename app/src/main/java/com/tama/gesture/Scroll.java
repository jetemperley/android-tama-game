package com.tama.gesture;

import com.tama.core.InputHandler;
import com.tama.util.Vec2;

public class Scroll extends GestureEvent
{
    Vec2<Float> prev = new Vec2<Float>(0f, 0f);
    Vec2<Float> next = new Vec2<Float>(0f, 0f);

    @Override
    public void callEvent(InputHandler handler)
    {
        handler.scroll(prev, next);
    }
}
