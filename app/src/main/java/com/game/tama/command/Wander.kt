package com.game.tama.command

import com.game.tama.core.World
import com.game.tama.thing.Pet

class Wander : State
{
    var waitTime = 0

    override fun update(world: World, pet: Pet)
    {
//        if (pet.queue.length() == 0)
//        {
//            var rand = Math.random()
//            if (rand < 0.25 * PetGame.gameSpeed / 1000f)
//            {
//                var moves: List<Vec2<Int>> = pet.getPossibleMoves(world, pet.loc.x, pet.loc.y)
//                val randDir = Direction.from(moves[Rand.RandInt(0, moves.size)])
//                pet.queue.add(CommandStep(randDir))
//            }
//
//        }
    }
}