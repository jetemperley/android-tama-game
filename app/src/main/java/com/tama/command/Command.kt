package com.tama.command

import com.tama.core.DisplayAdapter
import com.tama.core.World
import com.tama.thing.Pet
import java.io.Serializable

/**
 * An abtraction for any single action that a pet can execute
 */
abstract class Command : Serializable
{
    public var update: (pet: Pet, world: World) -> Unit = ::start
    public var state = CommandState.ready
    var actor: Pet? = null;

    protected open fun start(pet: Pet, world: World)
    {
        actor = pet;
        state = CommandState.doing;
        update = ::doing;
    }

    protected abstract fun doing(pet: Pet, world: World);

    open fun isReplaceable(): Boolean
    {
        return state == CommandState.ready;
    }

    open fun hardCancel()
    {
        state = CommandState.failed;
    }

    abstract fun draw(d: DisplayAdapter);
}