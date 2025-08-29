package com.game.tama.core.thing.tile;

import com.game.engine.Sprite;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;

public class BackpackSlot extends Tile
{
    @Override
    Sprite getAssets()
    {
        return Asset.sprites.get(AssetName.static_inv);
    }
}
