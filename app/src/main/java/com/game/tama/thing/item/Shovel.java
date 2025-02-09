package com.game.tama.thing.item;

import android.util.Log;

import com.game.tama.core.AssetName;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.thing.tile.TileType;

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

    public Thing use(World m, int ax, int ay)
    {

        Thing t = m.removeThing(ax, ay);
        TileType tt = m.getTile(ax, ay).type();
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
