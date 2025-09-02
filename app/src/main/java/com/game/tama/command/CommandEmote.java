package com.game.tama.command;

import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

public class CommandEmote extends Command
{
    @Override
    protected void start(final Pet pet, final World world)
    {
        super.start(pet, world);
        pet.anim.animId = 8;
        pet.anim.repeat = false;
        pet.anim.restart();
    }

    @Override
    protected void doing(final Pet pet, final World world)
    {
        if (!pet.anim.play)
        {
            pet.anim.animId = 0;
            pet.anim.restart();
            pet.anim.repeat = true;
            complete();
        }
    }
}
