package com.game.android.gesture;

import com.game.tama.util.Vec2;

public interface Input
{
    default public void singleTapConfirmed(float x, float y){}

    default public void singleDown(float x, float y){}

    default public void longPressConfirmed(float x, float y){}

    default public void doubleTapConfirmed(float x, float y){}

    default public void doubleTapRelease(float x, float y){}

    default public void doubleTapDragStart(float startX, float startY, float currentX, float currentY){}

    default public void doubleTapDrag(float prevX, float prevY, float nextX, float nextY){}

    default public void doubleTapDragEnd(float x, float y){}

    default public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2){}

    default public void dragStart(float x, float y){}

    default public void drag(Vec2<Float> prev, Vec2<Float> next){}

    default public void dragEnd(float x, float y){}
}
