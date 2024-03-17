package com.game.tama.thing;

import com.game.tama.core.Type;
import com.game.tama.core.Assets;
import com.game.tama.core.World;

public class Axe extends Thing
{

    Axe()
    {
        super();
        asset = Assets.Names.static_axe.name();
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing apply(World m, int ax, int ay)
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

