package com.game.android.gesture;

public class Down extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleDown(x, y);
    }
}
