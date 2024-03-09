package com.game.android.gesture;

import com.game.tama.core.Input;

public class DoubleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapConfirmed(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
