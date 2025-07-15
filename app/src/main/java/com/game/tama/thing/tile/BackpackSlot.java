package com.game.tama.thing.tile;

import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;

public class BackpackSlot extends Tile
{
    Sprite getAssets()
    {
        return Asset.sprites.get(AssetName.static_inv);
    }
}
