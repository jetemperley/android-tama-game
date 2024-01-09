package com.tama.gesture;

import com.tama.core.Interactive;
import com.tama.util.Vec2;

public class Scroll extends GestureEvent
{
    Vec2<Float> prev = new Vec2<Float>(0f, 0f);
    Vec2<Float> next = new Vec2<Float>(0f, 0f);

    @Override
    public void callEvent(Interactive handler)
    {
        handler.scroll(prev, next);
    }

    public void set(Vec2<Float> prev, Vec2<Float> next)
    {
        this.prev = prev;
        this.next = next;
    }
}
