package com.game.tama.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.AssetName;
import com.game.tama.core.WorldObject;
import com.game.android.Asset;
import com.game.tama.core.Sprite;

class LongGrass extends Tile
{

    WorldObject sprite2;
    WorldObject sprite3;

    LongGrass()
    {

        super();
        loc.flat = false;
        loc.yoff = 1;
        sprite2 = new WorldObject(loc.sprite);
        sprite2.yoff = -30;
        sprite2.flat = false;
        sprite3 = new WorldObject(loc.sprite);
        sprite3.yoff = -60;
        sprite3.flat = false;
    }

    Sprite getAssets()
    {
        Sprite d = Asset.getStaticSprite(AssetName.static_longgrass);
        if (sprite2 != null)
        {
            sprite2.sprite = d;
            sprite3.sprite = d;
        }
        return d;
    }

    public void setPos(int x, int y)
    {
        loc.setPos(x, y);
        sprite2.setPos(x, y);
        sprite3.setPos(x, y);
    }

    @Override public void draw(DisplayAdapter d)
    {

        super.draw(d);
        d.drawArr(sprite2);
        d.drawArr(sprite3);
    }

}
