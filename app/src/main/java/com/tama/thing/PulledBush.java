package com.tama.thing;

import com.tama.core.Assets;

class PulledBush extends Thing
{

    PulledBush()
    {
        super();
        asset = Assets.Names.static_pullbush.name();
        load();
    }
}
