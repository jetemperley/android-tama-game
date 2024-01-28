package com.tama.gesture;

import com.tama.core.Interactive;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.doubleTapRelease(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
