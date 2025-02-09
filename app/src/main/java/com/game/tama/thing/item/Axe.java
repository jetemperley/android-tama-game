package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;

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

    public Thing use(World m, int ax, int ay)
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

