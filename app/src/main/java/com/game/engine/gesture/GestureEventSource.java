package com.game.engine.gesture;

import com.game.engine.gesture.gestureEvent.GestureEvent;

public class GestureEventSource implements GestureEventHandler
{
    protected GestureEventHandler target;

    @Override
    public boolean handleEvent(GestureEvent event)
    {
        if (target == null)
            return false;
        return target.handleEvent(event);
    }

    public void setTarget(GestureEventHandler target)
    {
        this.target = target;
    }
}
