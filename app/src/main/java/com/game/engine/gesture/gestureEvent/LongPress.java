package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class LongPress extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.longPressConfirmed(x, y);
    }
}
