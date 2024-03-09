package com.game.tama.thing;

import com.game.tama.core.Assets;

class PulledBush extends Thing
{

    PulledBush()
    {
        super();
        asset = Assets.Names.static_pullbush.name();
        load();
    }
}
