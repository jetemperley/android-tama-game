package com.game.android.gesture;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDragEnd(x, y);
    }
}
