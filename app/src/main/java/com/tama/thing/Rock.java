package com.tama.thing;

import com.tama.core.Assets;

class Rock extends Thing
{

    Rock()
    {
        super();
        asset = Assets.static_rock;
        loadAsset();
    }

    public String getDescription()
    {
        return super.getDescription() + "Just a rock.";

    }
}
