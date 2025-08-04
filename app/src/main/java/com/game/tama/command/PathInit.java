package com.game.tama.command;

import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

public interface PathInit
{
    void run(CommandQueue queue, World world, Pet pet);
}
