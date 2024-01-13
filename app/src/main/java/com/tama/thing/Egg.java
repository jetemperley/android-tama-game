package com.tama.thing;

import com.tama.core.Animator;
import com.tama.core.Assets;
import com.tama.core.Displayable;
import com.tama.core.PetGame;
import com.tama.util.Rand;
import com.tama.core.World;

class Egg extends Thing
{

    Animator anim;
    int age;
    int hatchAge;

    Egg()
    {
        super();
        anim = new Animator(null);
        age = 0;
        hatchAge = 20000;

        asset = Assets.sheet_16_egg;
        loadAsset();

    }

    Displayable getAsset()
    {

        if (anim == null)
        {
            anim = new Animator(Assets.getSheet(asset));
            anim.play();
            anim.repeat(true);
        }
        else
        {
            anim.sheet = Assets.getSheet(asset);
        }
        return anim;
    }

    public void update(World map)
    {
        anim.update(this);
        if (!anim.play && Rand.RandInt(0, 100) < 20 * (PetGame.gameSpeed / 1000f))
        {
            anim.play();
        }
        age += PetGame.gameSpeed;
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
