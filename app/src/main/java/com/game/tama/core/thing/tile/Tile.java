package com.game.tama.core.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.engine.Sprite;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;
import com.game.tama.core.world.WorldObject;

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
        loc.isFlat = true;
    }

    Sprite getAssets()
    {
        return Asset.sheets.get(AssetName.sheet_16_terrain).getSprite(0, 0);
    }

    public void load()
    {
        loc.sprite = getAssets();
        if (thing != null)
        {
            thing.load();
        }
    }

    public void draw(final DisplayAdapter d)
    {
        if (visible)
        {
            d.draw(loc);
        }
        if (thing != null)
        {
            thing.draw(d);
        }
    }

    public void update(final World m)
    {
        if (thing != null)
        {
            thing.update(m);
        }
    }

    public void updateDetails(final World m)
    {

    }

    public void setVisible(final boolean visible)
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

    public void setThing(final Thing thing)
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
        final Thing t = thing.pickup();
        thing = null;
        return t;
    }

    public Thing getThing()
    {
        return thing;
    }

    public void setPos(final int x, final int y)
    {
        loc.setPos(x, y);
    }
}

