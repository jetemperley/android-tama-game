package com.game.tama.command

import com.game.android.DisplayAdapter
import com.game.tama.core.GameLoop
import com.game.tama.core.World
import com.game.tama.thing.Pet
import com.game.tama.thing.Thing

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
            return CommandFactory.commandWalkAndAdjacentAction(target,
                                                               CommandEat(target));
        }
    }
}