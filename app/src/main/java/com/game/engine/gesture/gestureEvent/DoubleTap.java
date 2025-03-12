package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class DoubleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapConfirmed(x, y);
    }
}
