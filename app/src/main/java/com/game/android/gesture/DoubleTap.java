package com.game.android.gesture;

public class DoubleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapConfirmed(x, y);
    }
}
