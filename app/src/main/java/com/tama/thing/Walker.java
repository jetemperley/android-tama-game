package com.tama.thing;

import com.tama.core.Assets;

public class Walker extends Pet
{

    public Walker()
    {
        super();
        asset = Assets.sheet_16_walker;
        loadAsset();
    }
}
