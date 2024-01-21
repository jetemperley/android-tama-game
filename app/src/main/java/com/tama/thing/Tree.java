package com.tama.thing;

import com.tama.R;
import com.tama.core.Animator;
import com.tama.core.Assets;
import com.tama.core.Displayable;
import com.tama.core.Type;
import com.tama.core.World;

public class Tree extends Thing implements java.io.Serializable
{
    int lvl = 0;
    int growth = 0;
    Animator anim;

    public Tree(int level)
    {
        super();
        asset = Assets.Names.static_poop.name();
        load();

        lvl = level;
        if (lvl < 0 || lvl > 4)
        {
            lvl = 0;
        }
        anim.animId = lvl;

    }

    Displayable getAsset()
    {
        if (anim == null)
        {
            anim = new Animator(Assets.sheets.get(R.drawable.sheet_16_treegrowth));
        }
        else
        {
            anim.sheet = Assets.sheets.get(R.drawable.sheet_16_treegrowth);
        }
        return anim;
    }


    public void update(World m)
    {
        //        if (growth < 1000) {
        //            growth ++;
        //        } else if (growth > 1000-2 && level < 2){
        //            level++;
        //            loc.sprite = Assets.sprites.get(16+level);
        //            growth = 0;
        //        }
        // Log.d("Tree", "" + growth);
        anim.update(this);
    }

    public void poke()
    {
        anim.play();
    }

    Type type()
    {
        return Type.tree;
    }

    public String getDescription()
    {
        return super.getDescription() + "Its a tree, level " + lvl + ".";
    }

    public Thing pickup()
    {
        return this;
    }

}
