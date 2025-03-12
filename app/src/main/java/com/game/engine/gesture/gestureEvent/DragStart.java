package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class DragStart extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.dragStart(x, y);
    }
}
