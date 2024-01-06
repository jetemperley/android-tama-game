package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.World;

class Seed extends Thing
{
    private boolean planted = false;

    Seed()
    {
        super();
        asset = Assets.static_seed;
        loadAsset();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing apply(World world, int ax, int ay)
    {
        Thing t = world.getThing(ax, ay);
        if (t == null || t == this)
        {
            planted = true;
            return null;
        }

        switch (t.type())
        {
            case pet:
                Pet p = (Pet) t;
                if (p.consume(t))
                {
                    return null;
                }
                return t;

            case food:

                break;
        }

        return this;
    }

    public String getDescription()
    {
        return super.getDescription() + "A seed, plant it and it will grow.";
    }

}
