package com.game.tama.thing.tile;

import com.game.android.Assets;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;

public class BackpackSlot extends Tile
{
    Sprite getAssets()
    {
        return Assets.getStaticSprite(AssetName.static_inv);
    }
}
