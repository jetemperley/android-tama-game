package com.game.tama.thing.pet;

import com.game.tama.core.Assets;
import com.game.tama.thing.pet.Pet;

// TODO remove this class and move to PetFactory

public class Walker extends Pet
{

    public Walker()
    {
        super();
        asset = Assets.Names.sheet_16_walker.name();
        load();
    }
}
