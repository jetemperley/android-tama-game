package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.SingleTapConfirmed;

public class InputStateIdle extends InputState
{
    PetGardenBehaviour gardenBehaviour;

    public InputStateIdle(final PetGardenBehaviour gardenBehaviour)
    {
        this.gardenBehaviour = gardenBehaviour;
    }

    @Override
    public Class<? extends InputState> scale(final Scale scale)
    {
        return InputStatePanning.class;
    }

    @Override
    public Class<? extends InputState> singleTapConfirmed(final SingleTapConfirmed tap)
    {
        gardenBehaviour.select(tap.x, tap.y);
        return getClass();
    }

    @Override
    public Class<? extends InputState> dragStart(final DragStart start)
    {
        if (!gardenBehaviour.isTouchingSelectedThing(start.x, start.y))
        {
            return InputStatePanning.class;
        }
        return InputStatePetting.class;
    }

}
