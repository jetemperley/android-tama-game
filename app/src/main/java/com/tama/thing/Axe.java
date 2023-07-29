package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.World;

public class Axe extends Thing
{

    Axe()
    {
        super();
        asset = Assets.static_axe;
        loadAsset();
    }

    boolean isItem()
    {
        return true;
    }

    Thing apply(World m, int ax, int ay)
    {

        Thing t = m.getThing(ax, ay);
        if (t == null)
        {
            return this;
        }

        switch (t.type())
        {
            case tree:

                Tree tree = (Tree) t;
                m.takeThing(ax, ay);
                if (tree.lvl == 2)
                {
                    m.add(new Wood(), tree.loc.x, tree.loc.y);
                }
                break;

            case food:

                break;

        }
        return this;
    }
}

