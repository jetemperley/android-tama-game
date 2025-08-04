package com.game.tama.core.thing.item;

import android.util.Log;

import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.thing.tile.TileType;
import com.game.tama.core.world.World;

class Shovel extends Thing
{

    Shovel()
    {
        super();
        asset = AssetName.static_shovel;
        load();
    }

    boolean isItem()
    {
        return true;
    }

    public Thing use(final World m, final int ax, final int ay)
    {

        final Thing t = m.removeThing(ax, ay);
        final TileType tt = m.getTile(ax, ay).type();
        Log.d("shovel", "apply" + tt);
        if (tt == TileType.water)
        {
            Log.d("shovel", "apply ground");
            m.setTile(ax, ay, TileType.grass);

        }
        else if (tt == TileType.grass)
        {
            Log.d("shovel", "apply water");
            m.setTile(ax, ay, TileType.water);
        }

        return this;
    }
}
