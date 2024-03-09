package com.game.tama.thing;

import com.game.tama.core.Assets;
import com.game.tama.core.Displayable;

public class BackpackSlot extends Tile
{
    Displayable getAssets()
    {
        return Assets.getSprite(Assets.Names.static_inv.name());
    }
}
