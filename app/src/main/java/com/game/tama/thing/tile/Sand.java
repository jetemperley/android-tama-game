package com.game.tama.thing.tile;

import com.tama.R;
import com.game.android.Assets;
import com.game.tama.core.Sprite;

public class Sand extends Tile
{

    Sprite getAssets()
    {
        return Assets.sheets.get(R.drawable.sheet_16_terrainsimp).getSprite(0, 2);
    }

}
