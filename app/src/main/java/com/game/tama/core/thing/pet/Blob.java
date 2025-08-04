package com.game.tama.core.thing.pet;

import com.game.tama.core.AssetName;

// TODO move to PetFactory

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = AssetName.sheet_16_blob;
        load();
    }
}
