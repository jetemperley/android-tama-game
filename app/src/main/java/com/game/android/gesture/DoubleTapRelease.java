package com.game.android.gesture;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapRelease(x, y);
    }
}
