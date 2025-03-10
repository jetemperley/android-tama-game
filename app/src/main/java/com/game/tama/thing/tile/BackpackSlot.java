package com.game.tama.thing.tile;

import com.game.android.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;

public class BackpackSlot extends Tile
{
    Sprite getAssets()
    {
        return Asset.getStaticSprite(AssetName.static_inv);
    }
}
