package com.game.android.gesture;

import com.game.tama.core.Input;
import com.game.tama.util.Vec2;

public class Scroll extends GestureEvent
{
    Vec2<Float> prev = new Vec2<Float>(0f, 0f);
    Vec2<Float> next = new Vec2<Float>(0f, 0f);

    @Override
    public void callEvent(Input handler)
    {
        handler.scroll(prev, next);
    }

    public void set(Vec2<Float> prev, Vec2<Float> next)
    {
        this.prev = prev;
        this.next = next;
    }

    @Override
    public Type type()
    {
        return Type.drag;
    }
}
