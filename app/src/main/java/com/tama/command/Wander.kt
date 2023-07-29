package com.tama.command

import com.tama.core.PetGame
import com.tama.thing.Direction
import com.tama.util.Rand
import java.util.*

class Wander : State
{
    var waitTime = 0

    override fun update(queue: CommandQueue)
    {
        if (queue.length() == 0)
        {
            var rand = Math.random()
            if (rand < 0.25 * PetGame.gameSpeed/1000f)
            {
                val randDir = Direction.values()[Rand.RandInt(0, 4)]
                queue.add(CommandStep(randDir))
            }

        }
    }
}