package com.game.engine.gesture;

import com.game.tama.util.Vec2;

public interface Input
{

    default void singleTapConfirmed(final float x, final float y) {}

    default void singleDown(final float x, final float y) {}

    default void longPressConfirmed(final float x, final float y) {}

    default void doubleTapConfirmed(final float x, final float y) {}

    default void doubleTapRelease(final float x, final float y) {}

    default void doubleTapDragStart(final float startX,
                                    final float startY,
                                    final float currentX,
                                    final float currentY)
    {}

    default void doubleTapDrag(final float prevX,
                               final float prevY,
                               final float nextX,
                               final float nextY)
    {}

    default void doubleTapDragEnd(final float x, final float y) {}

    default void scale(final Vec2<Float> p1,
                       final Vec2<Float> p2,
                       final Vec2<Float> n1,
                       final Vec2<Float> n2)
    {}

    default void dragStart(final float x, final float y) {}

    default void drag(final Vec2<Float> prev, final Vec2<Float> next) {}

    default void dragEnd(final float x, final float y) {}
}
