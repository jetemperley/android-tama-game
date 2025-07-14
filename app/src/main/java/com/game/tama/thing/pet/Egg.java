package com.game.tama.thing.pet;

import com.game.android.Asset;
import com.game.engine.Time;
import com.game.tama.core.Animator;
import com.game.tama.core.AssetName;
import com.game.tama.core.Sprite;
import com.game.tama.core.World;
import com.game.tama.thing.Thing;
import com.game.tama.util.Rand;

// TODO move to PetFactory and add functionality as component

class Egg extends Thing
{

    Animator anim;
    float age;
    int hatchAge;

    Egg()
    {
        super();
        anim = new Animator(null);
        age = 0;
        hatchAge = 20000;

        asset = AssetName.sheet_16_egg;
        load();

    }

    public Sprite getAsset()
    {

        if (anim == null)
        {
            anim = new Animator(Asset.getSpriteSheet(asset));
            anim.play();
            anim.repeat(true);
        }
        else
        {
            anim.sheet = Asset.getSpriteSheet(asset);
        }
        return anim;
    }

    public void update(final World map)
    {
        anim.update(this);
        if (!anim.play && Rand.RandInt(0, 100) < 20 * Time.deltaTimeS())
        {
            anim.play();
        }
        age += Time.deltaTimeS();
        if (age > hatchAge)
        {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), loc.x, loc.y);
        }
    }

    public String getDescription()
    {
        return super.getDescription() + "An egg, age " + age + ".";
    }

}
