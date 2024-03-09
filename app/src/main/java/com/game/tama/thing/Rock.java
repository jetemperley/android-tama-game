package com.game.tama.thing;

import com.game.tama.core.Assets;

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
