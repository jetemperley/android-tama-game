package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;

class Hammer extends Thing
{

    Hammer()
    {
        super();
        asset = AssetName.static_axe;
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing use(World m, int ax, int ay)
    {
        m.removeThing(ax, ay);
        return this;
    }
}
