package com.game.tama.core.thing.pet;

import com.game.engine.Sprite;
import com.game.engine.Time;
import com.game.tama.core.Animator;
import com.game.tama.core.Asset;
import com.game.tama.core.AssetName;
import com.game.tama.core.thing.Thing;
import com.game.tama.core.world.World;
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

    @Override
    public Sprite getAsset()
    {

        if (anim == null)
        {
            anim = new Animator(Asset.sheets.get(asset));
            anim.play();
            anim.repeat = true;
        }
        else
        {
            anim.sheet = Asset.sheets.get(asset);
        }
        return anim;
    }

    @Override
    public void update(final World map)
    {
        anim.update();
        if (!anim.play && Rand.RandInt(0, 100) < 20 * Time.deltaTime())
        {
            anim.play();
        }
        age += Time.deltaTime();
        if (age > hatchAge)
        {
            // hatch
            map.removeThing(this);
            map.add(new Blob(), loc.x, loc.y);
        }
    }

    @Override
    public String getDescription()
    {
        return super.getDescription() + "An egg, age " + age + ".";
    }

}
