package com.tama.thing;

import com.tama.core.Assets;

class Blob extends Pet
{

    Blob()
    {
        super();
        asset = Assets.sheet_16_blob;
        loadAsset();
    }
}
