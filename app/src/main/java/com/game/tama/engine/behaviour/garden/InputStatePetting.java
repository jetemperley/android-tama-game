package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.tama.util.Log;

public class InputStatePetting extends InputState
{
    PetGardenBehaviour gardenBehaviour;

    public InputStatePetting(final PetGardenBehaviour gardenBehaviour)
    {
        this.gardenBehaviour = gardenBehaviour;
    }

    @Override
    public Class<? extends InputState> drag(final Drag drag)
    {
        if (gardenBehaviour.isTouchingSelectedThing(drag.next.x, drag.next.y))
        {
            Log.log(this, "petting");
        }

        if (!gardenBehaviour.isTouchingSelectedThing(drag.next.x, drag.next.y)
            && gardenBehaviour.getSelected() == null)
        {
            return InputStatePanning.class;
        }

        if (gardenBehaviour.attemptPickup(drag.next.x, drag.next.y))
        {
            return InputStateHolding.class;
        }
        return InputStatePanning.class;
    }

    @Override
    public Class<? extends InputState> dragEnd(final DragEnd end)
    {
        return InputStateIdle.class;
    }
}
