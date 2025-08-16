package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class DoubleTapDragEnd extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.doubleTapDragEnd(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.doubleTapDragEnd(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.DOUBLE_TAP_DRAG_END;
    }
}
