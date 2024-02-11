package com.tama.core;

import android.view.MotionEvent;

import com.tama.gesture.GestureEventHandler;
import com.tama.util.Log;
import com.tama.util.Vec2;

public abstract class Interactive implements GestureEventHandler,
                                             Updateable, Drawable
{
    public void update() {}

    public void draw(DisplayAdapter display) {}

    public void scroll(Vec2<Float> prev, Vec2<Float> next) {}
}
