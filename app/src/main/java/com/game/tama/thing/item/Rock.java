package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.thing.Thing;

class Rock extends Thing
{

    Rock()
    {
        super();
        asset = AssetName.static_rock;
        load();
    }

    public String getDescription()
    {
        return super.getDescription() + "Just a rock.";

    }
}
