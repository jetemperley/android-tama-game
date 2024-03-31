package com.game.android.gesture;

public class LongPress extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.longPressConfirmed(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
