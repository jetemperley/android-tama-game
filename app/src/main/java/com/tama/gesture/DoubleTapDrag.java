package com.tama.gesture;

import com.tama.core.InputHandler;

public class DoubleTapDrag extends GestureEvent
{

    @Override
    public void callEvent(InputHandler handler)
    {
        handler.doubleTapDrag(x, y);
    }
}
