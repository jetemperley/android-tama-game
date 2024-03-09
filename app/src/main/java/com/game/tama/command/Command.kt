package com.game.tama.command

import android.util.Log
import com.game.android.DisplayAdapter
import com.game.tama.core.World
import com.game.tama.thing.Pet
import java.io.Serializable

/**
 * An abtraction for any single action that a pet can execute
 */
abstract class Command : Serializable
{
    public var update: (pet: Pet, world: World) -> Unit = ::start;
    public var state = CommandState.ready;
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
        update = ::noop;
        Log.d(javaClass.canonicalName, "canceled");
    }

    abstract fun draw(d: DisplayAdapter);

    fun fail()
    {
        state = CommandState.failed;
        update = ::noop
        Log.d(javaClass.canonicalName, "failed");
    }

    fun complete()
    {
        state = CommandState.complete
        update = ::noop
    }

    final fun noop(pet: Pet, world: World){}
}