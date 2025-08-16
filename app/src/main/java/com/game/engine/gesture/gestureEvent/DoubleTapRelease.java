package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class DoubleTapRelease extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.doubleTapRelease(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.doubleTapRelease(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.DOUBLE_TAP_RELEASE;
    }
}
