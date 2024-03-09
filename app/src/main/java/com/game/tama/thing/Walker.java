package com.game.tama.thing;

import com.game.tama.core.Assets;

public class Walker extends Pet
{

    public Walker()
    {
        super();
        asset = Assets.Names.sheet_16_walker.name();
        load();
    }
}
