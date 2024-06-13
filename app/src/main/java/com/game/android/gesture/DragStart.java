package com.game.android.gesture;

public class DragStart extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.dragStart(x, y);
    }
}
