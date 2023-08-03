package com.tama.command

import com.tama.core.Assets
import com.tama.core.DisplayAdapter
import com.tama.core.Displayable
import com.tama.thing.Pet
import com.tama.core.World
import java.util.*

/**
 * A self managing list commands that a pet will execute one at a time, regardless of command failures
 */
class CommandQueue : Command()
{
    private val queue: Queue<Command>
    private var failAction: () -> Unit

    init
    {
        update = ::start
        queue = PriorityQueue { a: Command, b: Command -> a.priority - b.priority }
        failAction = { queue.poll() }
    }

    override fun start(pet: Pet, world: World)
    {
        update = ::doing
    }

    override fun doing(pet: Pet, world: World): Unit
    {
        val current = queue.peek() ?: return
        current.update(pet, world)
        when (current.state)
        {
            CommandState.complete -> queue.poll();
            CommandState.failed   -> failAction();
        }
    }

    fun failAllOnFail(failAll: Boolean)
    {
        when (failAll)
        {
            true  -> failAction = { state = CommandState.failed }
            false -> failAction = { queue.poll() }
        }
    }

    fun length(): Int
    {
        return queue.size
    }

    fun add(command: Command)
    {
        queue.add(command);
    }

    fun draw(d: DisplayAdapter, pet: Displayable)
    {
        d.displayAbsolute(pet, 0f, 0f)
        for (i in 0..(queue.size-1))
        {
            d.displayAbsolute(Assets.getSprite(Assets.static_heart), (i + 1) * 16f, 0f)
        }
    }
}