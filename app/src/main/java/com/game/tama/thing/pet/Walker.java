package com.game.tama.thing.pet;

import com.game.tama.core.AssetName;

// TODO remove this class and move to PetFactory

public class Walker extends Pet
{

    public Walker()
    {
        super();
        asset = AssetName.sheet_16_walker;
        load();
    }
}
