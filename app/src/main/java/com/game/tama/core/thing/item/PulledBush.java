package com.game.tama.core.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;

class PulledBush extends Thing
{

    PulledBush()
    {
        super();
        asset = AssetName.static_pullbush;
        load();
    }
}
