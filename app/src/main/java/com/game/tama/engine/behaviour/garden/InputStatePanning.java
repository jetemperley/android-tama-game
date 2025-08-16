package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.ScaleRelease;

public class InputStatePanning extends InputState
{

    PetGardenBehaviour gardenBehaviour;

    public InputStatePanning(final PetGardenBehaviour gardenBehaviour)
    {
        this.gardenBehaviour = gardenBehaviour;
    }

    @Override
    public Class<? extends InputState> drag(final Drag drag)
    {
        gardenBehaviour.panWorld(drag.prev, drag.next);
        return getClass();
    }

    @Override
    public Class<? extends InputState> scale(final Scale scale)
    {
        gardenBehaviour.scaleView(scale.prev1, scale.prev2, scale.next1, scale.next2);
        return getClass();
    }

    @Override
    public Class<? extends InputState> dragEnd(final DragEnd end)
    {
        return InputStateIdle.class;
    }

    @Override
    public Class<? extends InputState> scaleRelease(final ScaleRelease release)
    {
        return InputStateIdle.class;
    }

}
