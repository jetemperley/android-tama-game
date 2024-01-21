package com.tama.thing;

import com.tama.core.Assets;
import com.tama.core.Displayable;

public class BackpackSlot extends Tile
{
    Displayable getAssets()
    {
        return Assets.getSprite(Assets.Names.static_inv.name());
    }
}
