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

    private final TileType type = TileType.water;

    public DynTile()
    {
        super();
        load();
    }

    Sprite getAssets()
    {
        if (sheet == null)
        {
            sheet = Asset.getSpriteSheet(AssetName.sheet_16_rect_index);
        }
        return null;
    }

    @Override
    public TileType type()
    {
        return type;
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

    @Override public void updateDetails(World world)
    {
        Tile topTile = world.getTile(loc.x, loc.y-1);
        Tile bottomTile = world.getTile(loc.x, loc.y+1);
        Tile leftTile = world.getTile(loc.x-1, loc.y);
        Tile rightTile = world.getTile(loc.x+1, loc.y);

        boolean top = topTile != null && topTile.type() == type;
        boolean bottom = bottomTile != null && bottomTile.type() == type;
        boolean left = leftTile != null && leftTile.type() == type;
        boolean right = rightTile != null && rightTile.type() == type;

        // index will be (top, bottom, left, right)
        byte index = 0;

        if (right)
        {
            index += 1;
        }
        if (left)
        {
            index += 1 << 1;
        }
        if (bottom)
        {
            index += 1 << 2;
        }
        if (top)
        {
            index += 1 << 3;
        }
        current = sheet.getSprite(0, index);
    }

}
