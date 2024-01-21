package com.tama.thing;

import com.tama.core.Assets;

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = Assets.Names.sheet_16_blob.name();
        load();
    }
}
