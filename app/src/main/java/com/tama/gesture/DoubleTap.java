package com.tama.gesture;

import com.tama.core.Interactive;

public class DoubleTap extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.doubleTapConfirmed(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
