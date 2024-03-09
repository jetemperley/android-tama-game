package com.game.android.gesture;

import com.game.tama.core.Input;

public class SingleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleTapConfirmed(x, y);
    }
    @Override
    public Type type()
    {
        return Type.press;
    }

}
