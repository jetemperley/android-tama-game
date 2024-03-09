package com.game.android.gesture;

import com.game.tama.core.Input;

public class Down extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleDown(x, y);
    }

    @Override
    public Type type()
    {
        return Type.press;
    }
}
