package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class SingleTapConfirmed extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.singleTapConfirmed(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.singleTapConfirmed(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.SINGLE_TAP_CONFIRMED;
    }
}
