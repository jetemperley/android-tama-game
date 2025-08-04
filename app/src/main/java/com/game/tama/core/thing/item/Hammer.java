package com.game.tama.core.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;

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

    public Thing use(final World m, final int ax, final int ay)
    {
        m.removeThing(ax, ay);
        return this;
    }
}
