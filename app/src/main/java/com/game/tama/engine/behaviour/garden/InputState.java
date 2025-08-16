package com.game.tama.engine.behaviour.garden;

import com.game.engine.gesture.gestureEvent.DoubleTapConfirmed;
import com.game.engine.gesture.gestureEvent.DoubleTapDrag;
import com.game.engine.gesture.gestureEvent.DoubleTapDragEnd;
import com.game.engine.gesture.gestureEvent.DoubleTapDragStart;
import com.game.engine.gesture.gestureEvent.DoubleTapRelease;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.GestureEvent;
import com.game.engine.gesture.gestureEvent.LongPressConfirmed;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.ScaleRelease;
import com.game.engine.gesture.gestureEvent.SingleTapConfirmed;

public abstract class InputState
{
    final Class<? extends InputState> handleEvent(final GestureEvent event)
    {
        return switch (event.getType())
        {
            case DOUBLE_TAP_CONFIRMED -> doubleTapConfirmed((DoubleTapConfirmed) event);
            case DOUBLE_TAP_DRAG -> doubleTapDrag((DoubleTapDrag) event);
            case DOUBLE_TAP_DRAG_END -> doubleTapDragEnd((DoubleTapDragEnd) event);
            case DOUBLE_TAP_DRAG_START -> doubleTapDragStart((DoubleTapDragStart) event);
            case DOUBLE_TAP_RELEASE -> doubleTapRelease((DoubleTapRelease) event);
            case DOWN -> singleDown((Down) event);
            case DRAG -> drag((Drag) event);
            case DRAG_END -> dragEnd((DragEnd) event);
            case DRAG_START -> dragStart((DragStart) event);
            case LONG_PRESS_CONFIRMED -> longPressConfirmed((LongPressConfirmed) event);
            case SCALE -> scale((Scale) event);
            case SINGLE_TAP_CONFIRMED -> singleTapConfirmed((SingleTapConfirmed) event);
            case SCALE_RELEASE -> scaleRelease((ScaleRelease) event);
            default -> throw new IllegalArgumentException(
                "Must implement input " + event.getClass().getSimpleName());
        };

    }

    public Class<? extends InputState> scaleRelease(final ScaleRelease release)
    {
        return this.getClass();
    }

    public Class<? extends InputState> singleTapConfirmed(final SingleTapConfirmed tap)
    {
        return this.getClass();
    }

    public Class<? extends InputState> singleDown(final Down down)
    {
        return this.getClass();
    }

    public Class<? extends InputState> longPressConfirmed(final LongPressConfirmed press)
    {
        return this.getClass();
    }

    public Class<? extends InputState> doubleTapConfirmed(final DoubleTapConfirmed tap)
    {
        return this.getClass();
    }

    public Class<? extends InputState> doubleTapRelease(final DoubleTapRelease release)
    {
        return this.getClass();
    }

    public Class<? extends InputState> doubleTapDragStart(final DoubleTapDragStart start)
    {
        return this.getClass();
    }

    public Class<? extends InputState> doubleTapDrag(final DoubleTapDrag drag)
    {
        return this.getClass();
    }

    public Class<? extends InputState> doubleTapDragEnd(final DoubleTapDragEnd end)
    {
        return this.getClass();
    }

    public Class<? extends InputState> scale(final Scale scale)
    {
        return this.getClass();
    }

    public Class<? extends InputState> dragStart(final DragStart start)
    {
        return this.getClass();
    }

    public Class<? extends InputState> drag(final Drag drag)
    {
        return this.getClass();
    }

    public Class<? extends InputState> dragEnd(final DragEnd end)
    {
        return this.getClass();
    }

}
