package com.tama.command

import com.tama.core.World
import com.tama.thing.Pet

interface State
{
    fun update(world: World, pet: Pet)
}