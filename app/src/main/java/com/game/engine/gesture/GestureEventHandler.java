package com.game.engine.gesture;

import com.game.engine.gesture.gestureEvent.GestureEvent;

public interface GestureEventHandler
{
    /**
     * Optionally consumes a gesture event
     *
     * @param event
     * @return return whether the event was consumed (true should stop
     * processing)
     */
    public boolean handleEvent(GestureEvent event);
}
