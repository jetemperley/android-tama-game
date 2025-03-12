package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class Down extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleDown(x, y);
    }
}
