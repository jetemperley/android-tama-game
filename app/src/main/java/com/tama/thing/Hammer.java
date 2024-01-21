package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.World;

class Hammer extends Thing
{

    Hammer()
    {
        super();
        asset = Assets.Names.static_axe.name();
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing apply(World m, int ax, int ay)
    {
        m.removeThing(ax, ay);
        return this;
    }
}
