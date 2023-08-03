package com.tama.thing;

import com.tama.apptest.R;
import com.tama.core.Assets;
import com.tama.core.Displayable;

public class Grass extends Tile
{

    Displayable getAssets()
    {
        return Assets.sheets.get(R.drawable.sheet_16_terrainsimp).getSprite(0, 0);
    }

    public TileType type()
    {
        return TileType.grass;
    }

}