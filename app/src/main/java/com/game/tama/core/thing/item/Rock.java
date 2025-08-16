package com.game.tama.core.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;

class Rock extends Thing
{

    Rock()
    {
        super();
        asset = AssetName.static_rock;
        load();
    }

    @Override
    public String getDescription()
    {
        return super.getDescription() + "Just a rock.";

    }
}
