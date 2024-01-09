package com.tama.gesture;

import com.tama.core.Interactive;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.doubleTapDragEnd(x, y);
    }
}
