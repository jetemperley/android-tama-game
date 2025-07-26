package com.game.tama.command.lambda;

import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

import java.io.Serializable;

@FunctionalInterface
public interface CommandUpdatable extends Serializable
{
    void update(Pet pet, World world);
}


