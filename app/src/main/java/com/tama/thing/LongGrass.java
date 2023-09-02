package com.tama.thing;

import com.tama.apptest.R;
import com.tama.core.Assets;
import com.tama.core.DisplayAdapter;
import com.tama.core.Displayable;
import com.tama.core.WorldObject;

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

    Displayable getAssets()
    {
        Displayable d = Assets.sprites.get(R.drawable.static_longgrass);
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

    @Override public void display(DisplayAdapter d)
    {

        super.display(d);
        d.displayWorld(sprite2);
        d.displayWorld(sprite3);
    }

}
