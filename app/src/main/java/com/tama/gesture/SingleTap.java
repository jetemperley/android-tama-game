package com.tama.gesture;

import com.tama.core.InputHandler;

public class SingleTap extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.singleTapConfirmed(x, y);
    }
}
