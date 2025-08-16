package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.InputEventMethod;
import com.game.engine.gesture.InputObjectMethod;

public class DragStart extends GestureEvent
{
    @Override
    public void callEventMethod(final InputEventMethod handler)
    {
        handler.dragStart(x, y);
    }

    @Override
    public void callObjectMethod(final InputObjectMethod handler)
    {
        handler.dragStart(this);
    }

    @Override
    public EventType getType()
    {
        return EventType.DRAG_START;
    }
}
