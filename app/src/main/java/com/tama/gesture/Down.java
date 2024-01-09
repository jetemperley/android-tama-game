package com.tama.gesture;

import com.tama.core.Interactive;

public class Down extends GestureEvent
{
    @Override
    public void callEvent(Interactive handler)
    {
        handler.singleDown(x, y);
    }
}
