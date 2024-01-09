package com.tama.gesture;

import com.tama.core.Interactive;

public class SingleTap extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.singleTapConfirmed(x, y);
    }
}
