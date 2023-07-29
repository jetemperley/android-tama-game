package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.World;

class Seed extends Thing
{

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

    Thing apply(World m, int ax, int ay)
    {

        Thing t = m.takeThing(ax, ay);
        if (t == null)
        {
            m.add(new Tree(0), ax, ay);
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
