package com.game.tama.command;

import com.game.engine.Time;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.pet.Pet;

public class CommandEat extends Command
{
    private final float totalTime = 1f;
    private float currentTime = 0f;
    private final Thing toEat;

    public CommandEat(final Thing targetToEat)
    {
        this.toEat = targetToEat;
    }

    @Override
    public void start(final Pet pet, final World world)
    {
        super.start(pet, world);
        world.removeThing(toEat);
        pet.children.add(toEat);
    }

    @Override
    protected void doing(final Pet pet, final World world)
    {
        currentTime += Time.deltaTimeS();

        if (currentTime > totalTime)
        {
            pet.children.remove(toEat);
            complete();
        }
    }

    public static Command Eat(final Thing target)
    {
        return CommandFactory.commandWalkAndAdjacentAction(
            target,
            new CommandEat(target));
    }
}
