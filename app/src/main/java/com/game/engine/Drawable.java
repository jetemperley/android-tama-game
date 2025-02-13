package com.game.engine;

import com.game.engine.DisplayAdapter;

public interface Drawable
{
    default public void draw(DisplayAdapter display){}
}
