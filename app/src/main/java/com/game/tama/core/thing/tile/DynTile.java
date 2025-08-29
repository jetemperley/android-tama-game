package com.game.tama.core.thing.tile;

import com.game.engine.DisplayAdapter;
import com.game.engine.Sprite;
import com.game.engine.SpriteSheet;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.world.World;

public class DynTile extends Tile
{
    // considers the surrounding tiles to create a dynamic tile graphic

    transient private SpriteSheet sheet;

    private final TileType type = TileType.water;

    public DynTile()
    {
        super();
        load();
    }

    @Override
    Sprite getAssets()
    {
        if (sheet == null)
        {
            sheet = Asset.sheets.get(AssetName.sheet_16_rect_index);
        }
        return null;
    }

    @Override
    public TileType type()
    {
        return type;
    }

    @Override
    public void draw(final DisplayAdapter d)
    {

        d.draw(loc);

        if (thing != null)
        {
            thing.draw(d);
        }
    }

    @Override
    public void updateDetails(final World world)
    {
        final Tile topTile = world.getTile(loc.x, loc.y - 1);
        final Tile bottomTile = world.getTile(loc.x, loc.y + 1);
        final Tile leftTile = world.getTile(loc.x - 1, loc.y);
        final Tile rightTile = world.getTile(loc.x + 1, loc.y);

        final boolean top = topTile != null && topTile.type() == type;
        final boolean bottom = bottomTile != null && bottomTile.type() == type;
        final boolean left = leftTile != null && leftTile.type() == type;
        final boolean right = rightTile != null && rightTile.type() == type;

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
        loc.sprite = sheet.getSprite(0, index);
    }

}
