package com.game.tama.command

import com.game.tama.core.World
import com.game.tama.thing.Pet
import java.io.Serializable

interface State : Serializable
{
    fun update(world: World, pet: Pet)
}