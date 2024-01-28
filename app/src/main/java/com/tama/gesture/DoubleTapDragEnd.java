package com.tama.gesture;

import com.tama.core.Interactive;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.doubleTapDragEnd(x, y);
    }

    @Override
    public Type type()
    {
        return Type.dragEnd;
    }
}
