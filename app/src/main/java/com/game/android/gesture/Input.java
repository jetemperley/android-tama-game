package com.game.android.gesture;

import com.game.tama.util.Vec2;

public interface Input
{
    public void singleTapConfirmed(float x, float y);

    public void singleDown(float x, float y);

    public void longPressConfirmed(float x, float y);

    public void doubleTapConfirmed(float x, float y);

    public void doubleTapRelease(float x, float y);

    public void doubleTapDragStart(float startX, float startY, float currentX, float currentY);

    public void doubleTapDrag(float prevX, float prevY, float nextX, float nextY);

    public void doubleTapDragEnd(float x, float y);

    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2);

    public void scroll(Vec2<Float> prev, Vec2<Float> next);
}
