package com.game.tama.command;

import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

public interface PathInit
{
    public void run(CommandQueue queue, World world, Pet pet);
}
