package com.game.tama.thing.pet;

import com.game.tama.core.Assets;
import com.game.tama.thing.pet.Pet;

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = Assets.Names.sheet_16_blob.name();
        load();
    }
}
