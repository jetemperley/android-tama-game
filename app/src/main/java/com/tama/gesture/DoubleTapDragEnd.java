package com.tama.gesture;

import com.tama.core.Input;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDragEnd(x, y);
    }

    @Override
    public Type type()
    {
        return Type.dragEnd;
    }
}
