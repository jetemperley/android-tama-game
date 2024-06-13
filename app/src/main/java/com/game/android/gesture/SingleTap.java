package com.game.android.gesture;

public class SingleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleTapConfirmed(x, y);
    }

}
