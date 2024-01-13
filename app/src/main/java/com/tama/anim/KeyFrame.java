package com.tama.anim;

import com.tama.util.Vec2;

public class KeyFrame
{
    final float time;
    final Vec2<Float> pos;

    public KeyFrame(float time, Vec2<Float> pos)
    {
        this.time = time;
        this.pos = pos;
    }
}
