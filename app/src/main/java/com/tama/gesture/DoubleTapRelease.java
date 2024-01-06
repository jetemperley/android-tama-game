package com.tama.gesture;

import com.tama.core.InputHandler;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.doubleTapRelease(x, y);
    }
}
