package com.tama.gesture;

import com.tama.core.InputHandler;

public class DoubleTap extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.doubleTapConfirmed(x, y);
    }
}
