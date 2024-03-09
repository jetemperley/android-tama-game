package com.game.tama.thing;

import com.game.tama.core.Assets;

class Wood extends Thing
{

    Wood()
    {
        asset = Assets.Names.static_log.name();
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public String getDescription()
    {
        return "A chunk of wood.";
    }
}
