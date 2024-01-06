package com.tama.gesture;

import com.tama.core.InputHandler;

public class Down extends GestureEvent
{
    @Override
    public void callEvent(InputHandler handler)
    {
        handler.singleDown(x, y);
    }
}
