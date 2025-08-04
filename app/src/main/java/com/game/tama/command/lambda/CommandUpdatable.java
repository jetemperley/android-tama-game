package com.game.tama.command.lambda;

import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

import java.io.Serializable;

@FunctionalInterface
public interface CommandUpdatable extends Serializable
{
    void update(Pet pet, World world);
}


