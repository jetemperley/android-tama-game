package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.GestureEventHandler;
import com.game.engine.gesture.gestureEvent.GestureEvent;

import java.util.HashMap;

public class GardenInputStateManger implements GestureEventHandler
{
    private final PetGardenBehaviour gardenBehaviour;
    InputState currentState;
    HashMap<Class<? extends InputState>, InputState> beans;

    public GardenInputStateManger(final PetGardenBehaviour gardenBehaviour)
    {
        this.gardenBehaviour = gardenBehaviour;
        beans = new HashMap<>();
        beans.put(InputStateIdle.class, new InputStateIdle(gardenBehaviour));
        beans.put(InputStateHolding.class, new InputStateHolding(gardenBehaviour));
        beans.put(InputStatePanning.class, new InputStatePanning(gardenBehaviour));
        beans.put(InputStatePetting.class, new InputStatePetting(gardenBehaviour));

        currentState = beans.get(InputStateIdle.class);
    }

    @Override
    public boolean handleEvent(final GestureEvent event)
    {
        final Class<? extends InputState> nextStateClass = currentState.handleEvent(event);
        final InputState nextState = beans.get(nextStateClass);
        if (nextState == null)
        {
            throw new IllegalStateException("Could not get state for " + nextStateClass);
        }
        if (nextState != currentState)
        {
            currentState.end(event);
            nextState.start(event);
        }
        currentState = nextState;
        return true;
    }
}
