package com.game.tama.anim;

import com.game.tama.util.Vec2;

import java.io.Serializable;

public class KeyFrame implements Serializable
{
    final float time;
    final Vec2<Float> pos;

    public KeyFrame(float time, Vec2<Float> pos)
    {
        this.time = time;
        this.pos = pos;
    }
}
