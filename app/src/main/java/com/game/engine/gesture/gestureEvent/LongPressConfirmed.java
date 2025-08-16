package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class LongPressConfirmed extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.longPressConfirmed(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.longPressConfirmed(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.LONG_PRESS_CONFIRMED;
    }
}
