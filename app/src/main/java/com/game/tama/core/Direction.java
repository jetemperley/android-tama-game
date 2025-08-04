package com.game.tama.core;

import com.game.tama.core.world.WorldObject;
import com.game.tama.util.Vec2;

/**
 * Defines the directions a thing can move in, and the relative x,y change for
 * that move; Additionally, each Direction.ordinal() will correspond to the
 * index for a spritesheet animation
 */
public enum Direction
{
    down(0, 1), up(0, -1), right(1, 0), left(-1, 0);

    public final int x, y;

    Direction(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public static Direction from(final Vec2<Integer> v)
    {
        return from(v.x, v.y);
    }

    public static Direction from(final int x, final int y)
    {
        if (x == 1 && y == 0)
        {
            return right;
        }
        else if (x == -1 && y == 0)
        {
            return left;
        }
        else if (x == 0 && y == 1)
        {
            return down;
        }
        else if (x == 0 && y == -1)
        {
            return up;
        }
        throw new RuntimeException(
            "Direction {" + x + " " + y + "} not a valid direction.");
    }

    public static Direction from(final WorldObject from, final WorldObject to)
    {
        return from(to.x - from.x, to.y - from.y);
    }
}