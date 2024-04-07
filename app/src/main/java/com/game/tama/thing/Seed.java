package com.game.tama.thing;

import com.game.tama.core.Type;
import com.game.tama.core.Assets;
import com.game.tama.core.World;

class Seed extends Thing
{
    private boolean planted = false;

    Seed()
    {
        super();
        asset = Assets.Names.static_seed.name();
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing use(World world, int ax, int ay)
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
