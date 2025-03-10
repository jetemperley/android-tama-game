package com.game.tama.command;

import com.game.engine.GameLoop;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.pet.Pet;

public class CommandEat extends Command
{
    private float totalTime = 1f;
    private float currentTime = 0f;
    private Thing toEat;

    public CommandEat(Thing targetToEat)
    {
        this.toEat = targetToEat;
    }

    @Override
    public void start(Pet pet, World world)
    {
        super.start(pet, world);
        world.removeThing(toEat);
        pet.children.add(toEat);
    }

    @Override
    protected void doing(Pet pet, World world)
    {
        currentTime += GameLoop.deltaTimeS;

        if (currentTime > totalTime)
        {
            pet.children.remove(toEat);
            complete();
        }
    }

    public static Command Eat(Thing target)
    {
        return CommandFactory.commandWalkAndAdjacentAction(
            target,
            new CommandEat(target));
    }
}
