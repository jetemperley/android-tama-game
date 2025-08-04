package com.game.tama.state;

import com.game.engine.Updateable;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

import java.io.Serializable;

public abstract class StateController implements Updateable, Serializable
{

    public abstract void update(Pet pet, World world);
}
