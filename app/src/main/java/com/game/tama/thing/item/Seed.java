package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.pet.Pet;

class Seed extends Thing
{
    private boolean planted = false;

    Seed()
    {
        super();
        asset = AssetName.static_seed;
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
