package com.tama.thing;

import android.util.Log;

import com.tama.core.Assets;
import com.tama.core.World;

class Shovel extends Thing
{

    Shovel()
    {
        super();
        asset = Assets.static_shovel;
        loadAsset();
    }

    boolean isItem()
    {
        return true;
    }

    Thing apply(World m, int ax, int ay)
    {

        Thing t = m.takeThing(ax, ay);
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
