package com.tama.gesture;

import com.tama.core.InputHandler;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.doubleTapDragEnd(x, y);
    }
}
