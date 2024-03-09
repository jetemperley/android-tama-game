package com.game.tama.thing;

import com.game.tama.core.Assets;

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = Assets.Names.sheet_16_blob.name();
        load();
    }
}
