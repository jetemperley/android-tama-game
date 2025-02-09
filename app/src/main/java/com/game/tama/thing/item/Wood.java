package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.thing.Thing;

class Wood extends Thing
{

    Wood()
    {
        asset = AssetName.static_log;
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
