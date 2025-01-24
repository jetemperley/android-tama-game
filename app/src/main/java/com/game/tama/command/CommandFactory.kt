package com.game.tama.command

import android.util.Log
import com.game.tama.core.World
import com.game.tama.core.WorldObject
import com.game.tama.core.Direction
import com.game.tama.thing.pet.Pet
import com.game.tama.thing.Thing
import com.game.tama.util.Path
import com.game.tama.util.Vec2

class CommandFactory
{

    companion object
    {

        fun commandPathTo(xTarget: Int,
                          yTarget: Int): CommandQueue
        {
            val command = CommandQueue(pathInitializer(xTarget, yTarget, 0));
            command.ultimateTarget = WorldObject(null);
            command.ultimateTarget!!.setPos(xTarget, yTarget);
            command.failAllOnFail(true);
            return command;
        }

        fun commandWalkAndAdjacentAction(target: Thing,
                                         adjacentCommand: Command): CommandQueue
        {
            val commandQueue = CommandQueue();
            commandQueue.failAllOnFail(true);
            val walk =
                    CommandQueue(pathInitializer(target.loc, 1))
            walk.failAllOnFail(true);
            commandQueue.add(walk);
            commandQueue.add(adjacentCommand);
            commandQueue.ultimateTarget = target.loc;
            return commandQueue;
        }

        private fun pathInitializer(target: WorldObject,
                                    dist: Int): (CommandQueue, World, Pet) -> Unit
        {
            return { queue: CommandQueue, world: World, pet: Pet ->
                pathInitializer(target.x, target.y, dist)(queue, world, pet);
            }
        }

        private fun pathInitializer(xTarget: Int,
                                    yTarget: Int,
                                    dist: Int): (CommandQueue, World, Pet) -> Unit
        {
            return { queue: CommandQueue, world: World, pet: Pet ->
                val path: Path = Path(dist);
                val steps: Array<Vec2<Int>> =
                        path.findPath(world,
                                      pet,
                                      pet.loc.x,
                                      pet.loc.y,
                                      xTarget,
                                      yTarget);

                var previous: Vec2<Int> = Vec2(pet.loc.x, pet.loc.y)
                for (next: Vec2<Int> in steps)
                {
                    val step: Vec2<Int> =
                            Vec2<Int>(next.x - previous.x, next.y - previous.y);
                    previous = next;
                    Log.d(javaClass.canonicalName, "${step.x} ${step.y}")
                    queue.add(CommandStep(Direction.from(step)));
                }
            }
        }
    }
}