package com.game.tama.thing.tile;

import com.game.tama.core.Assets;
import com.game.tama.core.Sprite;

public class BackpackSlot extends Tile
{
    Sprite getAssets()
    {
        return Assets.getSprite(Assets.Names.static_inv.name());
    }
}
