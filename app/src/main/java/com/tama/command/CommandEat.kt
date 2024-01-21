package com.tama.command

import com.tama.core.DisplayAdapter
import com.tama.core.GameLoop
import com.tama.core.PetGame
import com.tama.core.World
import com.tama.thing.Pet
import com.tama.thing.Thing
import com.tama.util.Log

class CommandEat(targetToEat: Thing) : Command()
{
    private val totalTime: Float = 1f;
    private var currentTime: Float = 0f;
    private val toEat: Thing = targetToEat;

    override fun start(pet: Pet, world: World)
    {
        super.start(pet, world);
        world.removeThing(toEat);
        pet.children.add(toEat);
    }

    override fun doing(pet: Pet, world: World)
    {
        currentTime += GameLoop.deltaTime;

        if (currentTime > totalTime)
        {
            pet.children.remove(toEat);
            complete();
        }
    }

    override fun draw(d: DisplayAdapter)
    {

    }

    companion object
    {
        fun Eat(target: Thing) : Command
        {
            return CommandFactory.commandWalkAndAdjacentAction(target, CommandEat(target));
        }
    }
}