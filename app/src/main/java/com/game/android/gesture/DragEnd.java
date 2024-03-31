package com.game.android.gesture;

public class DragEnd extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.dragEnd(x, y);
    }

    @Override
    public Type type()
    {
        return null;
    }
}
