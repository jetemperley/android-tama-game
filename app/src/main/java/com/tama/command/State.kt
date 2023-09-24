package com.tama.command

import com.tama.core.World
import com.tama.thing.Pet
import java.io.Serializable

interface State : Serializable
{
    fun update(world: World, pet: Pet)
}