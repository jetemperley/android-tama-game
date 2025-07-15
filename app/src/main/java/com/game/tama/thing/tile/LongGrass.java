package com.game.tama.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.WorldObject;

class LongGrass extends Tile
{

    WorldObject sprite2;
    WorldObject sprite3;

    LongGrass()
    {

        super();
        loc.isFlat = false;
        loc.yoff = 1;
        sprite2 = new WorldObject(loc.sprite);
        sprite2.yoff = -30;
        sprite2.isFlat = false;
        sprite3 = new WorldObject(loc.sprite);
        sprite3.yoff = -60;
        sprite3.isFlat = false;
    }

    Sprite getAssets()
    {
        final Sprite d = Asset.sprites.get(AssetName.static_longgrass);
        if (sprite2 != null)
        {
            sprite2.sprite = d;
            sprite3.sprite = d;
        }
        return d;
    }

    public void setPos(final int x, final int y)
    {
        loc.setPos(x, y);
        sprite2.setPos(x, y);
        sprite3.setPos(x, y);
    }

    @Override
    public void draw(final DisplayAdapter d)
    {

        super.draw(d);
        d.draw(sprite2);
        d.draw(sprite3);
    }

}
