package com.game.tama.core;

import com.game.tama.util.Vec2;

public interface Movement
{

    Vec2[] getMoves();
}

class OneStepAdj implements Movement
{

    static private Vec2[] steps;

    OneStepAdj()
    {
        if (steps == null)
        {
            steps = new Vec2[4];
            steps[0] = new Vec2(-1, 0);
            steps[1] = new Vec2(1, 0);
            steps[2] = new Vec2(0, 1);
            steps[3] = new Vec2(0, -1);
        }
    }

    public Vec2[] getMoves()
    {
        return steps;
    }
}
