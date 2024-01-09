package com.tama.gesture;

import com.tama.core.Interactive;

public class LongPress extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.longPressConfirmed(x, y);
    }
}
