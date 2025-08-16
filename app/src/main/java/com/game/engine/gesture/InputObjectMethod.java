package com.game.engine.gesture;

import com.game.engine.gesture.gestureEvent.DoubleTapConfirmed;
import com.game.engine.gesture.gestureEvent.DoubleTapDrag;
import com.game.engine.gesture.gestureEvent.DoubleTapDragEnd;
import com.game.engine.gesture.gestureEvent.DoubleTapDragStart;
import com.game.engine.gesture.gestureEvent.DoubleTapRelease;
import com.game.engine.gesture.gestureEvent.Down;
import com.game.engine.gesture.gestureEvent.Drag;
import com.game.engine.gesture.gestureEvent.DragEnd;
import com.game.engine.gesture.gestureEvent.DragStart;
import com.game.engine.gesture.gestureEvent.LongPressConfirmed;
import com.game.engine.gesture.gestureEvent.Scale;
import com.game.engine.gesture.gestureEvent.ScaleRelease;
import com.game.engine.gesture.gestureEvent.SingleTapConfirmed;

public interface InputObjectMethod
{

    default void singleTapConfirmed(final SingleTapConfirmed singleTap) {}

    default void singleDown(final Down down) {}

    default void longPressConfirmed(final LongPressConfirmed press) {}

    default void doubleTapConfirmed(final DoubleTapConfirmed doubleTap) {}

    default void doubleTapRelease(final DoubleTapRelease doubleTapRelease) {}

    default void doubleTapDragStart(final DoubleTapDragStart dragStart)
    {}

    default void doubleTapDrag(final DoubleTapDrag drag)
    {}

    default void doubleTapDragEnd(final DoubleTapDragEnd dragEnd) {}

    default void scale(final Scale scale)
    {}

    default void dragStart(final DragStart dragStart) {}

    default void drag(final Drag drag) {}

    default void dragEnd(final DragEnd dragEnd) {}

    default void scaleRelease(final ScaleRelease scaleRelease) {}
}
