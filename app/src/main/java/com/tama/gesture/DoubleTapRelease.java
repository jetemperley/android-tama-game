package com.tama.gesture;

import com.tama.core.Input;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapRelease(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
