package com.game.engine.gesture.gestureEvent;

import com.game.engine.gesture.Input;

public class SingleTap extends GestureEvent
{
    @Override
    public void callEvent(Input handler)
    {
        handler.singleTapConfirmed(x, y);
    }

}
