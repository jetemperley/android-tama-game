package com.tama.thing;

import com.tama.core.Assets;

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
