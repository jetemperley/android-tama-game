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

    public Thing apply(World m, int ax, int ay)
    {
        m.removeThing(ax, ay);
        return this;
    }
}
