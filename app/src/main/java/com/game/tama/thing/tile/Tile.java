package com.game.tama.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.WorldObject;
import com.game.tama.thing.Thing;
import com.tama.R;
import com.game.android.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.core.World;

public abstract class Tile implements java.io.Serializable
{

    WorldObject loc;
    boolean visible;
    protected Thing thing;

    Tile()
    {
        loc = new WorldObject(null);
        visible = true;
        loc.sprite = getAssets();
        loc.flat = true;
    }

    Sprite getAssets()
    {
        return Assets.sheets.get(R.drawable.sheet_16_terrain).getSprite(0, 0);
    }

    public void load()
    {
        loc.sprite = getAssets();
        if (thing != null)
        {
            thing.load();
        }
    }

    public void display(DisplayAdapter d)
    {
        if (visible)
        {
            d.drawArr(loc);
        }
        if (thing != null)
        {
            thing.draw(d);
        }
    }

    public void update(World m)
    {
        if (thing != null)
        {
            thing.update(m);
        }
    }

    public void updateDetails(World m)
    {

    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public TileType type()
    {
        return TileType.ground;
    }

    public boolean isEmpty()
    {
        return thing == null;
    }

    public void setThing(Thing thing)
    {
        this.thing = thing;
        if (thing != null)
        {
            thing.loc.setPos(loc.x, loc.y);
        }
    }

    public Thing removeThing()
    {
        if (thing == null)
        {
            return null;
        }
        Thing t = thing.pickup();
        thing = null;
        return t;
    }

    public Thing getThing()
    {
        return thing;
    }

    public void setPos(int x, int y)
    {
        loc.setPos(x, y);
    }
}

