package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.ScaleRelease;
import com.game.tama.engine.behaviour.GameManager;
import com.game.tama.util.Log;

public class InputStateHolding extends InputState
{
    PetGardenBehaviour gardenBehaviour;

    public InputStateHolding(final PetGardenBehaviour gardenBehaviour)
    {
        this.gardenBehaviour = gardenBehaviour;
    }

    @Override
    public Class<? extends InputState> scale(final Scale scale)
    {
        gardenBehaviour.scaleView(scale.prev1, scale.prev2, scale.next1, scale.next2);
        return getClass();
    }

    @Override
    public Class<? extends InputState> drag(final Drag drag)
    {
        GameManager.getHeldBehaviour().drag(drag.getParent(Drag.class).next);
        Log.log(this, "Dragging to " + drag.getParent(Drag.class).next);
        return getClass();
    }

    @Override
    public Class<? extends InputState> dragEnd(final DragEnd end)
    {
        final DragEnd parent = end.getParent(DragEnd.class);
        GameManager.getHeldBehaviour().dragEnd(parent.x, parent.y);
        return InputStateIdle.class;
    }

    @Override
    public Class<? extends InputState> scaleRelease(final ScaleRelease release)
    {
        final ScaleRelease parent = release.getParent(ScaleRelease.class);
        GameManager.getHeldBehaviour().dragEnd(parent.x, parent.y);
        return InputStateIdle.class;
    }
}
