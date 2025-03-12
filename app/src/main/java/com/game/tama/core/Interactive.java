package com.game.tama.core;

import com.game.engine.DisplayAdapter;
import com.game.engine.Drawable;
import com.game.engine.Updateable;
import com.game.tama.util.Vec2;
import com.game.engine.gesture.GestureEventHandler;

public abstract class Interactive implements GestureEventHandler,
                                             Updateable, Drawable
{
    public void update() {}

    public void draw(DisplayAdapter display) {}

    public void scroll(Vec2<Float> prev, Vec2<Float> next) {}
}
