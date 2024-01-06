package com.tama.core;

import android.view.MotionEvent;

import com.tama.gesture.GestureEventHandler;
import com.tama.util.Log;
import com.tama.util.Vec2;

public abstract class InputHandler extends GestureEventHandler
{
    public void update(){}

    public void draw(DisplayAdapter display){}

    public void singleTapConfirmed(float x, float y){}

    public void singleDown(float x, float y){}

    public void longPressConfirmed(float x, float y){}

    public void doubleTapConfirmed(float x, float y){}

    public void doubleTapRelease(float x, float y){}

    public void doubleTapDragStart(float startX, float startY, float currentX, float currentY){}

    public void doubleTapDrag(float x, float y){}

    public void doubleTapDragEnd(float x, float y){}

    public void scale(Vec2<Float> p1, Vec2<Float> p2, Vec2<Float> n1, Vec2<Float> n2){}

    public void scroll(Vec2<Float> prev, Vec2<Float> next){}
}
