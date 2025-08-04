package com.game.tama.core.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.pet.Pet;
import com.game.tama.core.world.World;

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

    public Thing use(final World world, final int ax, final int ay)
    {
        final Thing t = world.getThing(ax, ay);
        if (t == null || t == this)
        {
            planted = true;
            return null;
        }

        switch (t.type())
        {
            case pet:
                final Pet p = (Pet) t;
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
