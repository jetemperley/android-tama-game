package com.game.tama.core.thing.tile;

import com.game.engine.Sprite;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;

public class Grass extends Tile
{

    @Override
    Sprite getAssets()
    {
        return Asset.sheets.get(AssetName.sheet_16_terrainsimp).getSprite(0, 0);
    }

    @Override
    public TileType type()
    {
        return TileType.grass;
    }

}
