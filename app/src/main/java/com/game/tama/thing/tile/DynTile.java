package com.game.tama.thing.tile;

import com.game.android.DisplayAdapter;
import com.game.tama.core.WorldObject;
import com.tama.R;
import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.core.SpriteSheet;
import com.game.tama.core.World;

public class DynTile extends Tile
{
    // considers the surrounding tiles to create a dynamic tile graphic
    static SpriteSheet sheet;
    WorldObject[][] parts;


    // img is 4 px sq 4*3 sprite sheet of possible configurations
    public DynTile()
    {
        super();
        if (sheet == null)
        {
            sheet = Assets.sheets.get(R.drawable.sheet_8_watersimp);
        }

        parts = new WorldObject[2][2];

        for (int a = 0; a < parts.length; a++)
        {
            for (int b = 0; b < parts[a].length; b++)
            {

                parts[a][b] = new WorldObject(null);
                parts[a][b].xoff = a * 50;
                parts[a][b].yoff = b * 50;
            }
        }

    }

    Sprite getAssets()
    {

        if (sheet == null)
        {
            sheet = Assets.sheets.get(R.drawable.sheet_8_watersimp);
        }
        return null;

    }

    public TileType type()
    {
        return TileType.water;
    }

    public void display(DisplayAdapter d)
    {
        if (thing != null)
        {
            thing.draw(d);
        }

        for (int a = 0; a < parts.length; a++)
        {
            for (int b = 0; b < parts[a].length; b++)
            {
                if (parts[a][b].sprite != null)
                {
                    d.drawArr(parts[a][b]);
                }
            }
        }
    }

    public void setPos(int x, int y)
    {
        loc.setPos(x, y);
        for (int a = 0; a < parts.length; a++)
        {
            for (int b = 0; b < parts[a].length; b++)
            {
                parts[a][b].setPos(x, y);
            }
        }
    }

    @Override public void updateDetails(World m)
    {
        setTL(m);
        setTR(m);
        setBL(m);
        setBR(m);
    }

    private void setTL(World m)
    {

        parts[0][0].sprite = sheet.getSprite(2, 3);
        Tile t = m.getTile(loc.x - 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x, loc.y - 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x - 1, loc.y - 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l)
        {
            parts[0][0].sprite = sheet.getSprite(0, 0);
            if (u)
            {
                parts[0][0].sprite = sheet.getSprite(1, 3);

                if (ul)
                {
                    parts[0][0].sprite = null;
                }
                return;
            }
            return;
        }
        if (u)
        {
            parts[0][0].sprite = sheet.getSprite(0, 3);
        }
    }

    private void setTR(World m)
    {

        parts[1][0].sprite = sheet.getSprite(2, 0);
        Tile t = m.getTile(loc.x + 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x, loc.y - 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x + 1, loc.y - 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l)
        {
            parts[1][0].sprite = sheet.getSprite(0, 0);
            if (u)
            {
                parts[1][0].sprite = sheet.getSprite(1, 0);

                if (ul)
                {
                    parts[1][0].sprite = null;
                }
                return;
            }
            return;
        }
        if (u)
        {
            parts[1][0].sprite = sheet.getSprite(0, 1);
        }
    }

    private void setBL(World m)
    {

        parts[0][1].sprite = sheet.getSprite(2, 2);
        Tile t = m.getTile(loc.x - 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x, loc.y + 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x - 1, loc.y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l)
        {
            parts[0][1].sprite = sheet.getSprite(0, 2);
            if (u)
            {
                parts[0][1].sprite = sheet.getSprite(1, 2);
                if (ul)
                {
                    parts[0][1].sprite = null;
                }
                return;
            }
            return;
        }
        if (u)
        {
            parts[0][1].sprite = sheet.getSprite(0, 3);
        }
    }

    private void setBR(World m)
    {

        parts[1][1].sprite = sheet.getSprite(2, 1);
        Tile t = m.getTile(loc.x + 1, loc.y);
        boolean l = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x, loc.y + 1);
        boolean u = t != null && t.type() == TileType.water;
        t = m.getTile(loc.x + 1, loc.y + 1);
        boolean ul = t != null && t.type() == TileType.water;

        if (l)
        {
            parts[1][1].sprite = sheet.getSprite(0, 2);
            if (u)
            {
                parts[1][1].sprite = sheet.getSprite(1, 1);
                if (ul)
                {
                    parts[1][1].sprite = null;
                }
                return;
            }
            return;
        }
        if (u)
        {
            parts[1][1].sprite = sheet.getSprite(0, 1);
        }
    }

}
