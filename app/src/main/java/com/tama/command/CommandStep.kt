package com.tama.command

import com.tama.thing.Direction
import com.tama.thing.Pet
import com.tama.core.World

internal class CommandStep(var dir: Direction) : Command()
{
    public override fun start(pet: Pet, world: World)
    {
        pet.setDir(dir)
        val tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y)
        if (pet.canMoveOnto(tile))
        {
            world.removeThing(pet)
            world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y)
            pet.loc.xoff = -dir.x * 100;
            pet.loc.yoff = -dir.y * 100;
            update = ::doing
            return
        }
        state = CommandState.failed
    }

    public override fun doing(pet: Pet, world: World)
    {

        when
        {
            pet.loc.xoff != 0 ->
            {
                pet.loc.xoff += dir.x;
            }
            pet.loc.yoff != 0 ->
            {
                pet.loc.yoff += dir.y;
            }
            else              ->
                state = CommandState.complete;
        }
    }
}