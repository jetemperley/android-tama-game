package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.World;

class Hammer extends Thing
{

    Hammer()
    {
        super();
        asset = Assets.static_axe;
        loadAsset();
    }

    boolean isItem()
    {
        return true;
    }

    Thing apply(World m, int ax, int ay)
    {

        Thing t = m.takeThing(ax, ay);

        return this;
    }
}
