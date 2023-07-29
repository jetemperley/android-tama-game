package com.tama.command

import com.tama.thing.Pet
import com.tama.core.World

/**
 * An abtraction for any single action that a pet can execute
 */
abstract class Command
{
    public var update: (pet: Pet, world: World) -> Unit = ::start
    public var priority = 0
    public var state = CommandState.doing
    protected abstract fun start(pet: Pet, world: World)
    protected abstract fun doing(pet: Pet, world: World)
}