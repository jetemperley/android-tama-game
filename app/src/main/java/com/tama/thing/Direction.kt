package com.tama.thing

import com.tama.core.WorldObject
import com.tama.util.Vec2
import java.lang.Exception

/**
 * Defines the directions a thing can move in, and the relative x,y change for that move;
 * Additionally, each Direction.ordinal() will correspond to the index for a spritesheet animation
 */
enum class Direction(val x: Int, val y: Int)
{
    down(0, 1), up(0, -1), right(1, 0), left(-1, 0);

    companion object
    {
        fun from(v: Vec2<Int>): Direction
        {
            return when
            {
                v.x == 1 && v.y == 0 -> right
                v.x == -1 && v.y == 0 -> left
                v.y == 1 && v.x == 0 -> down
                v.y == -1 && v.x == 0 -> up
                else -> throw Exception()
            }
        }

        fun from(from: WorldObject, to: WorldObject): Direction
        {
            val x = to.x - from.x;
            val y = to.y - from.y;
            return when
            {
                x == 1 && y == 0 -> right
                x == -1 && y == 0 -> left
                y == 1 && x == 0 -> down
                y == -1 && x == 0 -> up
                else -> throw Exception()
            }
        }
    }
}