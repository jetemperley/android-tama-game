package com.tama.command

import com.tama.core.DisplayAdapter
import com.tama.core.GameActivity
import com.tama.core.PetGame
import com.tama.core.World
import com.tama.thing.Pet
import com.tama.thing.Thing
import com.tama.util.Log

class Eat(var toEat: Thing) : Command()
{
    val totalTime: Int = 1000;
    var currentTime: Int = 0;

    override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        world.removeThing(toEat);
        pet.secondaryThings.add(toEat);
    }

    override fun doing(pet: Pet, world: World)
    {
        currentTime += PetGame.gameSpeed;
        Log.log(this, "time is $currentTime");
        if (currentTime > totalTime)
        {
            pet.secondaryThings.remove(toEat);
            update = {pet, world -> ;}
            state = CommandState.complete;
        }
    }

    override fun draw(d: DisplayAdapter)
    {

    }

}