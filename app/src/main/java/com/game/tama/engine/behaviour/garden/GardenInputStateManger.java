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
            handleEvent(event);
        }

        return true;
    }

    //    @Override
    //    public void singleTapConfirmed(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.singleTapConfirmed(x, y));
    //    }
    //
    //    @Override
    //    public void singleDown(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.singleDown(x, y));
    //    }
    //
    //    @Override
    //    public void longPressConfirmed(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.longPressConfirmed(x, y));
    //    }
    //
    //    @Override
    //    public void doubleTapConfirmed(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.doubleTapConfirmed(x, y));
    //    }
    //
    //    @Override
    //    public void doubleTapRelease(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.doubleTapRelease(x, y));
    //    }
    //
    //    @Override
    //    public void doubleTapDragStart(final float startX,
    //                                   final float startY,
    //                                   final float currentX,
    //                                   final float currentY)
    //    {
    //        currentState =
    //            beans.get(currentState.doubleTapDragStart(startX, startY, currentX, currentY));
    //    }
    //
    //    @Override
    //    public void doubleTapDrag(final float prevX,
    //                              final float prevY,
    //                              final float nextX,
    //                              final float nextY)
    //    {
    //        currentState =
    //            beans.get(currentState.doubleTapDrag(prevX, prevY, nextX, nextY));
    //    }
    //
    //    @Override
    //    public void doubleTapDragEnd(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.doubleTapDragEnd(x, y));
    //
    //    }
    //
    //    @Override
    //    public void scale(final Vec2<Float> p1,
    //                      final Vec2<Float> p2,
    //                      final Vec2<Float> n1,
    //                      final Vec2<Float> n2)
    //    {
    //        currentState = beans.get(currentState.scale(p1, p2, n1, n2));
    //
    //    }
    //
    //    @Override
    //    public void dragStart(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.dragStart(x, y));
    //
    //    }
    //
    //    @Override
    //    public void drag(final Vec2<Float> prev, final Vec2<Float> next)
    //    {
    //        currentState = beans.get(currentState.drag(prev, next));
    //    }
    //
    //    @Override
    //    public void dragEnd(final float x, final float y)
    //    {
    //        currentState = beans.get(currentState.dragEnd(x, y));
    //    }
}
