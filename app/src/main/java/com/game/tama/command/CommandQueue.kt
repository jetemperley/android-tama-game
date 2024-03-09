package com.game.tama.command

import android.util.Log
import com.game.android.DisplayAdapter
import com.game.tama.thing.Pet
import com.game.tama.core.World
import com.game.tama.core.WorldObject
import com.game.tama.util.Vec2
import java.util.*

/**
 * A self managing list commands that a pet will execute one at a time,
 * regardless of command failures unless failAllOnFail is set to true
 */
class CommandQueue constructor() : Command()
{
    private val queue: Queue<Command>;
    private var failAction: () -> Unit;
    private var initialiser: (CommandQueue, World, Pet) -> Unit =
            { queue, world, pet ->
                Log.d(this.javaClass.canonicalName, "default initiliser");
            };
    var ultimateTarget: WorldObject? = null;

    constructor(initializer: (CommandQueue, World, Pet) -> Unit) : this()
    {
        Log.d(this.javaClass.canonicalName, "initilizing queue");
        this.initialiser = initializer;
    }

    init
    {
        update = ::start;
        queue = LinkedList()
        failAction = { queue.poll() }
    }

    override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        Log.d(this.javaClass.canonicalName, "starting queue");
        initialiser(this, world, pet);
    }

    override fun doing(pet: Pet, world: World): Unit
    {
        val current = queue.peek();
        if (current == null)
        {
            state = CommandState.complete;
            return;
        }
        current.update(pet, world)
        when (current.state)
        {
            CommandState.complete -> queue.poll();
            CommandState.failed   ->
            {
                Log.d(this.javaClass.canonicalName, "failed queue element");
                failAction()
            };
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

    override fun draw(d: DisplayAdapter)
    {
        if (actor == null || ultimateTarget == null)
            return;
        val start: Vec2<Float> = actor!!.loc.worldArrPos;
        start.x += 1;
        start.y += 1;
        val end: Vec2<Float> = ultimateTarget!!.worldArrPos;
        end.x += 1;
        end.y += 1;
        d.drawLine((start.x * 16 - 8),
                   (start.y * 16 - 8),
                   (end.x * 16 - 8),
                   (end.y * 16 - 8));
    }

    override fun isReplaceable(): Boolean
    {
        val current: Command? = queue.peek();
        return current == null || current.isReplaceable();
    }

    override fun hardCancel()
    {
        val command: Command? = queue.peek();
        command?.hardCancel();
        super.hardCancel();
    }
}