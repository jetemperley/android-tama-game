package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapRelease(x, y);
    }
}
