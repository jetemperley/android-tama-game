package com.tama.thing;

import com.tama.core.Assets;

public class Walker extends Pet
{

    public Walker()
    {
        super();
        asset = Assets.Names.sheet_16_walker.name();
        loadAsset();
    }
}
