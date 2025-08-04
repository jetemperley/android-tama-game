package com.game.tama.core.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;

public class Axe extends Thing
{

    Axe()
    {
        super();
        asset = AssetName.static_axe;
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing use(final World m, final int ax, final int ay)
    {

        final Thing t = m.getThing(ax, ay);
        if (t == null)
        {
            return this;
        }

        switch (t.type())
        {
            case tree:

                final Tree tree = (Tree) t;
                m.removeThing(ax, ay);
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

