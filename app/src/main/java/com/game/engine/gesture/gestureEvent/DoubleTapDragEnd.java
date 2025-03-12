package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.doubleTapDragEnd(x, y);
    }
}
