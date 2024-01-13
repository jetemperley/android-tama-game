package com.tama.thing;

import com.tama.core.Animator;
import com.tama.core.Assets;
import com.tama.core.Displayable;
import com.tama.core.World;

public class Bush extends Thing
{

    Animator anim;

    public Bush()
    {
        asset = Assets.sheet_16_bush;
        loadAsset();
    }

    Displayable getAsset()
    {

        if (anim == null)
        {
            anim = new Animator(Assets.getSheet(asset));
        }
        else
        {
            anim.sheet = Assets.getSheet(asset);
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
