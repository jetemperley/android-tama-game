package com.game.tama.core.thing.item;

import com.game.engine.Sprite;
import com.game.tama.core.Animator;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.Type;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;

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

    @Override
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

    @Override
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

    @Override
    public void poke()
    {
        anim.play();
    }

    @Override
    public Type type()
    {
        return Type.tree;
    }

    @Override
    public String getDescription()
    {
        return super.getDescription() + "Its a tree, level " + lvl + ".";
    }

    @Override
    public Thing pickup()
    {
        return this;
    }

}
