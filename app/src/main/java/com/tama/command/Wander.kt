package com.tama.command

import com.tama.core.PetGame
import com.tama.core.World
import com.tama.thing.Direction
import com.tama.thing.Pet
import com.tama.util.Rand
import com.tama.util.Vec2
import java.util.*

class Wander : State
{
    var waitTime = 0

    override fun update(world: World, pet: Pet)
    {
        if (pet.commandQueue.length() == 0)
        {
            var rand = Math.random()
            if (rand < 0.25 * PetGame.gameSpeed / 1000f)
            {
                var moves: List<Vec2<Int>> = pet.getPossibleMoves(world, pet.loc.x, pet.loc.y)
                val randDir = Direction.from(moves[Rand.RandInt(0, moves.size)])
                pet.commandQueue.add(CommandStep(randDir))
            }

        }
    }
}