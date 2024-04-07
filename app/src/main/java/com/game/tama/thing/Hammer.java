package com.game.tama.thing;

import com.game.tama.core.Assets;
import com.game.tama.core.World;

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

    public Thing use(World m, int ax, int ay)
    {
        m.removeThing(ax, ay);
        return this;
    }
}
