package com.tama.command

import android.util.Log
import com.tama.core.DisplayAdapter
import com.tama.thing.Direction
import com.tama.thing.Pet
import com.tama.core.World
import com.tama.core.WorldObject

public class CommandStep(var dir: Direction) : Command()
{

    public override fun start(pet: Pet, world: World)
    {
        super.start(pet, world)
        Log.d(javaClass.canonicalName, "stepping " + dir.x + " " + dir.y);
        pet.setDir(dir)
        val tile = world.getTile(pet.loc.x + dir.x, pet.loc.y + dir.y)
        if (pet.canMoveOnto(tile))
        {
            pet.setMovementPose(Pet.Movement.walk);
            world.removeThing(pet)
            world.add(pet, pet.loc.x + dir.x, pet.loc.y + dir.y)
            pet.loc.xoff = -dir.x * 100;
            pet.loc.yoff = -dir.y * 100;
            update = ::doing
            return
        }
        Log.d(javaClass.canonicalName, "failed step");
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
            {
                pet.setMovementPose(Pet.Movement.stand);
                state = CommandState.complete;
            }
        }
    }

    override fun hardCancel()
    {
        super.hardCancel()
        actor?.loc?.xoff = 0;
        actor?.loc?.yoff = 0;
    }

    override fun draw(d: DisplayAdapter)
    {

    }
}