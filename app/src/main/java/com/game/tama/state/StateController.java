package com.game.tama.state;

import com.game.tama.core.Updateable;
import com.game.tama.core.World;
import com.game.tama.thing.pet.Pet;

import java.io.Serializable;

public abstract class StateController implements Updateable, Serializable
{

    public abstract void update(Pet pet, World world);
}
