package com.tama.thing;

import com.tama.R;
import com.tama.core.Assets;
import com.tama.core.Displayable;

public class Sand extends Tile
{

    Displayable getAssets()
    {
        return Assets.sheets.get(R.drawable.sheet_16_terrainsimp).getSprite(0, 2);
    }

}
