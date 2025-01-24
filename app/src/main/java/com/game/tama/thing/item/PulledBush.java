package com.game.tama.thing.item;

import com.game.tama.core.Assets;
import com.game.tama.thing.Thing;

class PulledBush extends Thing
{

    PulledBush()
    {
        super();
        asset = Assets.Names.static_pullbush.name();
        load();
    }
}
