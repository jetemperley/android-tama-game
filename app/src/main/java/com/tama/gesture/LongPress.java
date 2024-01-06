package com.tama.gesture;

import com.tama.core.InputHandler;

public class LongPress extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.longPressConfirmed(x, y);
    }
}
