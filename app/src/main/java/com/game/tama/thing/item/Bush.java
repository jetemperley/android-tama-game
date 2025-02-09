package com.game.tama.thing.item;

import com.game.tama.core.AssetName;
import com.game.tama.core.Animator;
import com.game.tama.core.World;
import com.game.android.Assets;
import com.game.tama.core.Sprite;
import com.game.tama.thing.Thing;

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
            anim = new Animator(Assets.getSpriteSheet(asset));
        }
        else
        {
            anim.sheet = Assets.getSpriteSheet(asset);
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
        Thing t = new PulledBush();
        t.loc.setPos(loc.x, loc.y);
        return t;
    }

    @Override public void update(World map)
    {
        anim.update(this);
    }
}
