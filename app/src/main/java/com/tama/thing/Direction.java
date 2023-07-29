package com.tama.thing;

/**
 * Defines the directions a thing can move in, and the relative x,y change for that move;
 * Additionally, each Direction.ordinal() will correspond to the index for a spritesheet animation
 */
public enum Direction
{
    down(0, -1), right(1, 0), up(0, 1), left(-1, 0);

    public final int x;
    public final int y;

    Direction(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
