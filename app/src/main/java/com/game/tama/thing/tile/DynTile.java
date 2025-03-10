package com.game.tama.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.tama.core.AssetName;
import com.game.tama.core.StaticSprite;
import com.game.android.Asset;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.World;

public class DynTile extends Tile
{
    // considers the surrounding tiles to create a dynamic tile graphic
    private SpriteSheet sheet;
    private StaticSprite current;

    public DynTile()
    {
        super();
        load();
    }

    Sprite getAssets()
    {
        if (sheet == null)
        {
            sheet = Asset.getSpriteSheet(AssetName.sheet_16_rect);
        }
        return null;
    }

    public TileType type()
    {
        return TileType.water;
    }

    public void draw(DisplayAdapter d)
    {
        if (current != null)
        {
            d.drawSprite(current, loc.x, loc.y);
        }
        if (thing != null)
        {
            thing.draw(d);
        }
    }

    @Override public void updateDetails(World m)
    {
    }


}
