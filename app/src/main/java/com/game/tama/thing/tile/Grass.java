package com.game.tama.thing.tile;

import com.game.tama.core.AssetName;
import com.game.android.Asset;
import com.game.tama.core.Sprite;

public class Grass extends Tile
{

    Sprite getAssets()
    {
        return Asset.getSpriteSheet(AssetName.sheet_16_terrainsimp).getSprite(0, 0);
    }

    public TileType type()
    {
        return TileType.grass;
    }

}
