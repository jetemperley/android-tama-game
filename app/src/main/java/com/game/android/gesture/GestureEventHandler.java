package com.game.android.gesture;

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
