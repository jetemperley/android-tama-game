package com.game.tama.core.thing.item;

import com.game.tama.core.Animator;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;

public class Bush extends Thing
{

    Animator anim;

    public Bush()
    {
        asset = AssetName.sheet_16_bush;
        load();
    }

    @Override
    public Sprite getAsset()
    {
        if (anim == null)
        {
            anim = new Animator(Asset.sheets.get(asset));
        }
        else
        {
            anim.sheet = Asset.sheets.get(asset);
        }
        anim.animId = 1;
        anim.animDur = 500;
        return anim;
    }

    public void poke()
    {
        anim.play();
    }

    boolean isItem()
    {
        return true;
    }

    public String getDescription()
    {
        return "A little bush.";
    }

    public Thing pickup()
    {
        final Thing t = new PulledBush();
        t.loc.setPos(loc.x, loc.y);
        return t;
    }

    @Override
    public void update(final World map)
    {
        anim.update(this);
    }
}
