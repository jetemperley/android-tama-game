package com.game.engine;

import java.io.Serializable;

public class KeyFrame<T> implements Serializable
{
    final float time;
    final T frame;

    public KeyFrame(final float time, final T frame)
    {
        this.time = time;
        this.frame = frame;
    }
}
