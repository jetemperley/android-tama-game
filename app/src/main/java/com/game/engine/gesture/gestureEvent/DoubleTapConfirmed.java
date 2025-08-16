package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class DoubleTapConfirmed extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.doubleTapConfirmed(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.doubleTapConfirmed(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.DOUBLE_TAP_CONFIRMED;
    }
}
