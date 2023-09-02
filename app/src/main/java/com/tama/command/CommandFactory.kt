package com.tama.command

import android.util.Log
import com.tama.core.World
import com.tama.thing.Direction
import com.tama.thing.Pet
import com.tama.util.Path
import com.tama.util.Vec2

class CommandFactory
{
    companion object
    {

        fun commandPathTo(xTarget: Int,
                          yTarget: Int): CommandQueue
        {
            val command = CommandQueue(pathInitializer(xTarget, yTarget));
            command.ultimateTarget = Vec2<Int>(xTarget, yTarget);
            return command;
        }

        private fun pathInitializer(xTarget: Int,
                                    yTarget: Int): (CommandQueue, World, Pet) -> Unit
        {
            return { queue: CommandQueue, world: World, pet: Pet ->
                val path: Path = Path(0);
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