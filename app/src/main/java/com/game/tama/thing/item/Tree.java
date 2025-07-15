package com.game.tama.thing.item;

import com.game.tama.core.Animator;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.Type;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;

public class Tree extends Thing implements java.io.Serializable
{
    int lvl = 0;
    int growth = 0;
    Animator anim;

    public enum GrowthLevel
    {
        sprout_0,
        sapling_1,
        medium_2,
        large_3,
        dead_4
    }

    public Tree(final GrowthLevel level)
    {
        super();
        asset = AssetName.static_poop;
        load();

        lvl = level.ordinal();
        if (lvl < 0 || lvl > 4)
        {
            lvl = 0;
        }
        anim.animId = lvl;

    }

    public Sprite getAsset()
    {
        if (anim == null)
        {
            anim = new Animator(Asset.sheets.get(AssetName.sheet_16_treegrowth));
        }
        else
        {
            anim.sheet = Asset.sheets.get(AssetName.sheet_16_treegrowth);
        }
        return anim;
    }


    public void update(final World m)
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

    public Type type()
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
