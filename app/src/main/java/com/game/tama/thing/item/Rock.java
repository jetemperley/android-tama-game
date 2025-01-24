package com.game.tama.thing.item;

import com.game.tama.core.Assets;
import com.game.tama.thing.Thing;

class Rock extends Thing
{

    Rock()
    {
        super();
        asset = Assets.Names.static_rock.name();
        load();
    }

    public String getDescription()
    {
        return super.getDescription() + "Just a rock.";

    }
}
