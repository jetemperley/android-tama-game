package com.game.tama.core;

import com.game.android.DisplayAdapter;
import com.game.tama.util.Vec2;
import com.game.android.gesture.GestureEventHandler;

public abstract class Interactive implements GestureEventHandler,
                                             Updateable, Drawable
{
    public void update() {}

    public void draw(DisplayAdapter display) {}

    public void scroll(Vec2<Float> prev, Vec2<Float> next) {}
}
